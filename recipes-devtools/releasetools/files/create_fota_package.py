#!/usr/bin/env python

# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

import argparse
import collections
import logging
import os
import re
from subprocess import Popen, PIPE
import shutil
import site
import sys
import tempfile
import xml.etree.ElementTree as ET
site.addsitedir(os.path.dirname(os.path.abspath(__file__)))

logger = logging.getLogger(__name__)

if not logger.handlers:
    """_summary_: setting up of Loggers levels
    """
    # execute only first time this module is importedm to prevent creation of
    # multiple logger handlers
    syslog = logging.StreamHandler()
    format = r'%(asctime)s %(filename)s %(lineno)s %(levelname)s: %(message)s'
    formatter = logging.Formatter(format)
    syslog.setFormatter(formatter)
    logger.setLevel(logging.INFO)
    logger.addHandler(syslog)


class EmptyFileError(Exception):
    """File provided is empty"""
    pass


class ShellCMDException(Exception):
    """Script failed to execute the shell command"""
    pass


def etree_to_dict(node):
    """
    Convert an element tree to dictionary
    """
    dictionary = {node.tag: {} if node.attrib else None}
    children = list(node)

    if children:
        dd = collections.defaultdict(list)
        for dc in map(etree_to_dict, children):
            for key, val in list(dc.items()):
                dd[str(key)].append(val)
        dictionary = {
            node.tag: {str(key): val[0] if len(val) == 1 else val
                       for key, val in list(dd.items())}}
    if node.attrib:
        dictionary[node.tag].update((str(k), v) for k, v in list(node.attrib.items()))
    if node.text:
        text = node.text.strip()
        if children or node.attrib:
            dictionary[node.tag]['#text'] = text
        else:
            dictionary[node.tag] = text
    return dictionary


def parse_commandline_args():
    """
    Commandline argument definitions.
    """
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter,
        description='Get recent Completed metabuilds')
    parser.add_argument(
        '-m', '--meta-loc', action='append',
        type=str, required=True,
        help='Metabuild from which the FOTA package should be generated.')
    parser.add_argument(
        '--dest-loc', default=None,
        help='Creates FOTA in desired location')
    parser.add_argument(
        '--mirror-copy', action='store_true', default=False,
        help='If enabled, package generated with mirror copy')
    parser.add_argument(
        '--auth-enabled', action='store_true', default=False,
        help='If enabled, package generated with package authentication')
    parser.add_argument(
        '--is-package-name', action='store_true', default=False,
        help='To generate SWP as per the M-Plane specifications\
        (ORAN Section 8.3.1 Software package name')
    parser.add_argument(
        '--build-type',
        help='Creates FOTA with package name by build_type')
    parser.add_argument(
        '--maintainance-release',
        help='Creates FOTA with package name by maintenance release.')
    parser.add_argument(
        '--ru-type',
        help='Creates FOTA with package name by ru-type. 32TB/64TB')
    parser.add_argument(
        '--update-release',
        help='Creates FOTA with package name by update_release ')
    parser.add_argument(
        '--vendor-code',
        help='Creates FOTA with unique code of the vendor for SWP')
    parser.add_argument(
        '--retain-workspace', action='store_true', default=False,
        help='If enabled, workspace will not be deleted')
    return parser.parse_args()


def copy_file(source, destination):
    """Copies file or dir.

    Args:
        source (file/dir):file/dir to be copied
        destination (dir): where to be copied
    """
    try:
        if os.path.isdir(source):
            shutil.copytree(source, destination)
        else:
            shutil.copy(source, destination)
    except Exception as e:
        logger.error("Error occurred while copying file.{}".format(str(e)))


def get_config(chipset="Lassen"):
    """setting up Lassen configs
    Returns:
        target path
    """
    if chipset == "Lassen":
        apps_proc = 'apps_proc'
        distro = 'build-qti-distro-base-debug'
        image_name = 'cinder'
        return os.path.join(apps_proc, distro, 'tmp-glibc/deploy/images',
                            image_name)


def run_shell_cmd(cmd):
    """running shell commands
    Args:
        cmd (list): command to be executed
    Returns:
        _type_: output of command runs.
    """
    out = Popen(cmd, stdout=PIPE, stderr=PIPE, universal_newlines=True)
    (output, error) = out.communicate()
    logger.debug('output:{}'.format(output))
    if out.returncode != 0:
        logger.info("Error occured while running:{}".format(cmd))
        raise ShellCMDException("Error:{}".format(error))
    return output


class FOTA(object):
    def __init__(self, meta_loc=None, dest_loc=None):
        self.cwd = os.getcwd()
        self.meta = None
        self.meta_loc = meta_loc
        self.contents_xml = os.path.join(meta_loc, 'contents.xml')
        self.dest_loc = dest_loc
        self.ota_loc = os.path.join(self.dest_loc, 'ota-scripts')
        self.nh_tmp_dir = ''
        self.lib = os.path.join(meta_loc, 'common/build/lib')
        self.apps_path = self.get_apps_path()
        if not os.path.exists(self.dest_loc):
            os.mkdir(self.dest_loc)
            logger.info('Workspace does not exists, creating...')
        else:
            logger.info('Existing workspace there.....')
        if not os.path.exists(self.ota_loc):
            self.image_path = get_config()
            ota_cd = os.path.join(self.apps_path, self.image_path, "ota-scripts")
            logger.info("ota workspace {}".format(ota_cd))
            copy_file(ota_cd, self.ota_loc)
            logger.info('ota-scripts not there, copying...')

    def get_ab_files(self, file):
        """gets nhlos ab files list
        Args:
            file (xml): partition xml
        Returns:
            dict: AB partition files list
        """
        root = self.xml_root(file)
        ab_partition_dict = dict()
        for x in root['configuration']['physical_partition']['partition']:
            if x.get('filename'):
                if ('_b' in x['label']) and (type(x['filename'] == str)):
                    ab_partition_dict[x['filename']] = x['label']
        return ab_partition_dict

    def get_partition_xml_from_meta_cli(self, storage_type, meta_loc):
        """getting partition_xml path through meta_cli.py
        Args:
            storage_type (str): emmc/nand
            meta_loc (str): meta location
        Returns:
            str: partition.xml path
        """
        try:
            partition_xml = ''
            meta_cli_path = "{}/common/build/app/meta_cli.py".format(meta_loc)
            if os.path.exists(meta_cli_path):
                meta_api_call = ['python', meta_cli_path, 'get_files', 'attr=raw_partition',
                                 'storage={}'.format(storage_type)]
                partition_xml = run_shell_cmd(meta_api_call).strip().split("\"")[1].strip()
                return partition_xml
            else:
                logger.info("meta_cli.py returned partition.xml as None")
        except Exception as e:
            logger.error('Error while copy_radio_files: {}'.format(e))

    def get_partition_xml_by_loc(self, storage_type, meta_loc):
        """getting partition_xml path from meta location
        Args:
            storage_type (str): emmc/nand
            meta_loc (str): meta location
        Returns:
            str: partition.xml path
        """
        try:
            partition_xml = '{meta_loc}/common/config/{storage_type}\
                            /partition.xml'.format(
                            meta_loc=meta_loc, storage_type=storage_type)
            if not os.path.exists(partition_xml):
                partition_xml = '{meta_loc}/common/config/partition.xml'\
                                .format(meta_loc=meta_loc)
            if not os.path.exists(partition_xml):
                partition_xml = '{meta_loc}/common/build/partition.xml'\
                                .format(meta_loc=meta_loc)
            return partition_xml
        except Exception as e:
            logger.error('Error copy_radio_files: {}'.format(e))

    def get_partition_xml_path(self, storage_type):
        """Get partition XML path from contents.xml
        Args:
            storage_type (_type_): emmc/nand
        Returns:
            str: path of partition.xml
        """
        try:
            partition_xml = self.get_partition_xml_from_meta_cli(storage_type, self.meta_loc)
            if partition_xml:
                logger.info("partition_xml {}".format(partition_xml))
            else:
                partition_xml = self.get_partition_xml_by_loc(storage_type, self.meta_loc)
                logger.info("finally partition.xml path: {}".format(partition_xml))
        except Exception as e:
            logger.error('Error copy_radio_files: {}'.format(e))
        return partition_xml

    def copy_partition_images(self, unzipd, nh_bins_dir,
                              ab_partition_filenames):
        """Copying nhlos "_b" bins to Radio folder
        Args:
            unzipd (str): ota workspace
            nh_bins_dir (str): ab_partition file locations
            ab_partition_filenames (list): ab_partition_filenames
        """
        unsupported_radio_images = ['tools.fv', 'system.img', 'boot.img',
                                            'cache.img', 'persist.img',
                                            'systemrw.img', 'userdata.img']
        for image_name, partition_name in ab_partition_filenames.items():
            if image_name in unsupported_radio_images:
                continue
            ab_partition_image = partition_name.replace('_b', '')
            bins_dict = {}
            bins_dict[image_name] = partition_name
            try:
                copy_file('{}/{}'.format(nh_bins_dir, image_name),
                          os.path.join(unzipd, 'RADIO', ab_partition_image))
                copy_file('{}/{}'.format(nh_bins_dir, image_name),
                          os.path.join(unzipd, 'IMAGES', ab_partition_image))
            except Exception as e:
                logger.exception(str(e))

    def calling_ab_fota(self, unzipd, nh_bins_dir, storage_type):
        """AB_ota procedure
        Args:
            unzipd (str): ota workspace
            nh_bins_dir (str): ab_partition file locations
            storage_type (str): emmc/nand
        """
        try:
            partition_xml = self.get_partition_xml_path(storage_type)
            logger.info("################ starting abgota #################")
            ab_partition_filenames = self.get_ab_files(partition_xml)
            self.copy_partition_images(unzipd, nh_bins_dir,
                                       ab_partition_filenames)
        except Exception as e:
            raise Exception('Error A/B in fota {}'
                            .format(e))

    def get_metainfo(self):
        """metainfo summary
        Returns:
            dict: meta info in form of dictionary
        """
        if os.path.exists(self.lib):
            sys.path.append(self.lib)
        # Get meta build info from contents XML
        import meta_lib as metalib
        metainfo = metalib.meta_info(logger=None, file_pfn=self.contents_xml)
        return metainfo

    def get_radio_image_asdict(self):
        """Getting Radio images
        Returns:
            dict: list of nhlos files mapped with images
        """
        metainfo = self.get_metainfo()
        # Fetch list of files based on fastboot attributes
        var_list = list()
        var_file_list = {}
        var_file_list_fbc = metainfo.get_file_vars(attr="fastboot_complete",
                                                   flavor='asic')
        var_file_list_fb = metainfo.get_file_vars(attr="fastboot",
                                                  flavor='asic')
        var_list.append(var_file_list_fbc)
        var_list.append(var_file_list_fb)
        logger.info(var_list)
        for dict in var_list:
            for key, val in list(dict.items()):
                var_file_list.setdefault(key, list())
                var_file_list[key] += val

        return var_file_list

    def get_radio_image(self):
        """Getting Radio images
        Returns:
            list: list of nhlos files
        """
        radio_image_file_list = []
        # Fetch radio image files list as dict
        unsupported_radio_images = ['tools.fv', 'system.img', 'boot.img',
                                            'cache.img', 'persist.img',
                                            'systemrw.img', 'userdata.img']
        var_file_list = self.get_radio_image_asdict()
        for key, file in sorted(var_file_list.items()):
            is_radio_image = True
            if file[-1].split('/') in unsupported_radio_images:
                is_radio_image = False
            # Filtered out RADIO image list
            if is_radio_image:
                radio_image_file_list.extend(file)
        return radio_image_file_list

    def copy_nh_bins_to_dir(self, meta_loc, nh_tmp_dir):
        """copying of NonAB nhlos bins to RADIO folder
        Args:
            meta_loc (str): meta location
            nh_tmp_dir (str): tmp dir from where nhlos to be copied
        """
        meta_bins = self.get_radio_image()
        meta_bins_new = []
        for i in meta_bins:
            if i.find('keymaster64') != -1:
                meta_bins_new.append(i.replace('keymaster64', 'keymaster'))
            else:
                meta_bins_new.append(i)
        meta_bins = meta_bins_new
        try:
            temp_nhlos_bin = os.path.join(nh_tmp_dir, 'metainfo_filecache')
            os.mkdir(temp_nhlos_bin)
        except Exception as e:
            raise logger.exception("Error while copying file{}".format(str(e)))
        # Copy NON-HLOS bin to temp folder
        for binfile in meta_bins:
            logger.info("Copy {} > {}".format(binfile, temp_nhlos_bin))
            copy_file(binfile, temp_nhlos_bin)
        return meta_bins

    def get_build_path_from_contentsxml(self):
        """return values of build key from root dict
        Args:
            root (dict): root of contents xml
        Returns:
            dict: values inside ['contents']['builds_flat']['build']
        """
        root = self.xml_root(self.contents_xml)
        return root['contents']['builds_flat']['build']

    def get_apps_path(self):
        """Getting Apps path from contents.xml
        Returns:
            str: returns Apps path
        """
        for item in self.get_build_path_from_contentsxml():
            if item['name'] == 'apps' and item['linux_root_path']:
                return item['linux_root_path'].get('#text')

    def get_meta(self):
        """Getting META name
        Returns:
            str: returns META name
        """
        for item in self.get_build_path_from_contentsxml():
            if item['name'] == 'common':
                return item['build_id']

    def copy_nhlosbins_tmp_loc(self, meta_loc):
        """coying of nhlos to temp location
        Args:
            meta_loc (str): meta location
        """
        try:
            nh_tmpd = tempfile.mkdtemp(prefix='fotabins_{}'.format(
                self.get_meta()))
            self.copy_nh_bins_to_dir(meta_loc, nh_tmpd)
            self.nh_tmp_dir = os.path.join(nh_tmpd, 'metainfo_filecache')
        except Exception as e:
            logger.exception(str(e))
            raise e

    def copy_manifest(self, meta_loc, dest_loc):
        """To copy manifest file to RADIO folder
        Args:
            meta_loc (str): meta location
            dest_loc (str): Radio folder
        """
        copy_file(os.path.join(
            meta_loc, "common/build/emmc/bin/asic/manifest.xml"), dest_loc)

    def copy_nhlos_files(self, meta_loc, target_zip):
        """Makes Radio folder ready
        Args:
            meta_loc (str): meta_loc
            target_zip (str): raw target file
        Returns:
            str: unzipped target location
        """
        unzipd = "{}/unzipped_{}".format(self.dest_loc, self.get_meta())
        unzip_cmd = ['unzip', '-qo', target_zip, '-d', unzipd]
        logger.info("Unzip command is {}".format(unzip_cmd))
        run_shell_cmd(unzip_cmd)
        radio = '{}/RADIO'.format(unzipd)
        src_loc = r"{}".format(self.nh_tmp_dir)
        dest_loc = r"{}".format(radio)
        for file_name in os.listdir(src_loc):
            src_file = os.path.join(src_loc, file_name)
            dest_file = os.path.join(dest_loc, file_name)
            try:
                copy_file(src_file, dest_file)
                logger.info("NHLOS bins Copied {}".format(file_name))
            except Exception as e:
                logger.info("NHLOS failed to Copy {}".format(file_name))
                logger.error('Error: {}'.format(e))
        self.calling_ab_fota(unzipd, self.nh_tmp_dir, "emmc")
        self.copy_manifest(meta_loc, radio)
        return unzipd

    def prepare_target_file(self, meta, meta_loc, target_zip):
        """Zip and prepare the target file
        Args:
            meta (str): meta name
            meta_loc (str): meta location
            target_zip (str): raw target file
        Returns:
            str: return packed target zip
        """
        copy_file(target_zip, '{}/target_{}.zip'.format(self.dest_loc, meta))
        self.copy_nhlosbins_tmp_loc(meta_loc)
        unzipd = self.copy_nhlos_files(meta_loc, target_zip)
        os.chdir(unzipd)
        target = '{}/target_{}.zip'.format(self.dest_loc, meta)
        zip_cmd = ['zip', '-qry', target, '.']
        logger.info("zip command is {}".format(zip_cmd))
        run_shell_cmd(zip_cmd)
        os.chdir(self.cwd)
        return target

    def create_fota_package(self, meta_loc, target_zip):
        """Main  FOTA creation func
        Args:
            meta_loc (str): meta location
            target_zip (str): target zip file
        """
        self.meta = self.get_meta()
        target_loc = self.prepare_target_file(self.meta, meta_loc, target_zip)
        return target_loc

    def xml_root(self, file):
        """Return root of xml file
        Args:
            file: xml file to be parsed
        Returns:
            root none
        """
        tree = ET.parse(file)
        root = tree.getroot()
        root = etree_to_dict(root)
        return root

def copy_fota_zip_to_dest(dest_loc):
    """copy FOTA zip to destination location by user

    Sample ota: MA32TBORU234431DS20231205.zip
    VENDOR_SPECIFIC_FIELD = RU_TYPE + "ORU" + YY + Q + UPDATE_RELEASE
    + MAINTENENCE_RELEASE + RELEASE_BUILD_TYPE + YYYY + MM + DD
    Args:
        dest_location
    """
    cwd = os.getcwd()
    file_match = None
    new_file = r"([A-Z]{2}[0-9]{2}[A-Z]{2}ORU[0-9]{2}[1-4]{1}[0-9]{2,3}[0-9]{1,3}[A-Z]{1,2}[0-9]{4}[0-9]{2}[0-9]{2}.zip$)"
    for file_name in os.listdir(cwd):
        file_match = re.search(new_file, file_name)
        if file_match:
            break
    if file_match:
        new_file = file_match.group()
        new_tgtfiles_zip = os.path.join(cwd, file_match.group())
    else:
        new_file = 'update_ext4.zip'
        new_tgtfiles_zip = os.path.join(cwd, new_file)
    copy_file(new_tgtfiles_zip, dest_loc)
    logger.info("Final ota-zip is located at {}/{}".format(dest_loc, new_file))

def create_ws():
    """Creating default destination location
    Returns:
        str: dest loc
    """
    dest_loc = os.path.join('/tmp', 'Fota_location')
    if not os.path.exists(dest_loc):
        os.mkdir(dest_loc)
        logger.info('Workspace does not exists, creating...')
    else:
        logger.info('Existing workspace there.....')
    return dest_loc

def get_package_creation_cmd(is_package_name, vendor_code, update_release,
                           ru_type, build_type, maintainance_release):
    """Package name formation
    Args:
        is_package_name (bool):
        vendor_code (str):vendor_code
        update_release (str):update_release
        ru_type (str):ru_type
        build_type (str):build_type
        maintainance_release (str):maintainance_release
    """
    package_name_cmd = list()
    if is_package_name:
        package_name_cmd.append("--package_name")
        if vendor_code:
            package_name_cmd.extend(["--vendor_code", vendor_code])
        if update_release:
            package_name_cmd.extend(["--update_release", update_release])
        if ru_type:
            package_name_cmd.extend(["--ru_type", ru_type])
        if build_type:
            package_name_cmd.extend(["--build_type", build_type])
        if maintainance_release:
            package_name_cmd.extend(["--maintainance_release",
                                maintainance_release])
    return package_name_cmd

def run_ota_cmd(target_list, dest_loc, ota_workspace, package_name=None):
    """Final OTA command
    Args:
        target_list (str): list containing prepared target location.
        dest_loc (str): dest loc
        ota_workspace (str): raw workspace
        package_name (str): ORAN achitecture driven parameters
    """
    cwd = os.getcwd()
    os.chdir(os.path.join(ota_workspace, 'ota-scripts'))
    mplane_full_ota = './mplane_full_ota.sh'
    run_cmd = [mplane_full_ota, str(len(target_list))]
    run_cmd.extend(target_list)
    run_cmd.extend(['./rootfs', 'ext4', '--block',
                     '--system_path', '/', '--install_only'])
    run_cmd.extend(package_name)
    logger.info(run_cmd)
    run_shell_cmd(run_cmd)
    copy_fota_zip_to_dest(dest_loc)
    os.chdir(cwd)

def get_target_list(meta_loc, ota_workspace):
    """Return target list after packing nhlos
    Args:
        meta_loc (str): meta_loc
        ota_workspace (): raw workspace
    Returns:
        target_list (str): list containing prepared target location.
    """
    target_list = list()
    for meta in meta_loc:
        if meta == '' or meta is None:
            raise EmptyFileError('empty meta location provided')
        fota = FOTA(meta, ota_workspace)
        image = 'qti-csm-image'
        target_zip = os.path.join(meta, get_config(), image,
                                  'target-files-ext4.zip')
        logger.info("target {}".format(target_zip))
        tar = fota.create_fota_package(meta, target_zip)
        target_list.append(tar)
    return target_list


def main():
    args = parse_commandline_args()
    meta_loc = args.meta_loc
    retain_workspace = args.retain_workspace
    is_package_name = args.is_package_name
    build_type = args.build_type
    maintainance_release = args.maintainance_release
    ru_type = args.ru_type
    update_release = args.update_release
    vendor_code = args.vendor_code
    dest_loc = args.dest_loc
    target_list = list()
    package_name = list()
    ota_workspace = None
    logger.info("inside main function")

    try:
        if dest_loc is None:
            dest_loc = '/tmp'
            ota_workspace = create_ws()
        else:
            ota_workspace = os.path.join(dest_loc, 'Fota_location')
        target_list = get_target_list(meta_loc, ota_workspace)
        if is_package_name:
            package_name = get_package_creation_cmd(
                is_package_name, vendor_code, update_release,
                ru_type, build_type, maintainance_release)
        run_ota_cmd(target_list, dest_loc, ota_workspace, package_name)
    except Exception as e:
        raise Exception('Error executing FOTA script, {}'.format(e))
    finally:
        if not retain_workspace:
            shutil.rmtree(ota_workspace)


if __name__ == '__main__':
    main()
