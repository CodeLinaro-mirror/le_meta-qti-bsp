#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear
#

def find_incremental_pkg(original, latest):
    incremental = []

    if len(original) == 0 or len(latest) == 0:
        bb.error("Empty manifest! Please check!")

    for i in range(len(latest)):
        if latest[i] not in original:
            incremental.append(latest[i])
    return incremental

def copy_incrimantal_ipk(original,incrimantal, d):
    # These package suffixes are taken from the definitions of
    # PACKAGES and PACKAGES_DYNAMIC in meta/conf/bitbake.conf
    pn_suffixs = ['', '-dbg', '-dev', '-doc', '-staticdev', '-locale']

    incremental_pkgs = find_incremental_pkg(original, incrimantal)
    incremental_ipk_dir = os.path.join(d.getVar('DEPLOY_DIR'), 'artifacts','packages')
    bb.note("incremental_ipk_dir: %s" % incremental_ipk_dir)
    if os.path.exists(incremental_ipk_dir):
        oe.path.remove(incremental_ipk_dir)
    bb.utils.mkdirhier(incremental_ipk_dir)

    remove_package="qim-sysroot ros-sysroot"

    for pkg in incremental_pkgs:
        if pkg.split()[0] in remove_package :
            bb.note("remove the no pkgs %s" % pkg)
        else :
          if len(pkg.split()):
            for suffix in pn_suffixs:
                # PF: Specifies the recipe or package name and includes all version and revision numbers
                # (i.e. glibc-2.13-r20+svnr15508/ and bash-4.2-r1/). This variable is comprised of the
                # following: ${PN}-${EXTENDPE}${PV}-${PR}
                file_name = pkg.split()[0] + suffix + '_' + pkg.split()[2].split(":", 1)[-1] + '_' + pkg.split()[1] + '.ipk'
                file_full_path = os.path.join(d.getVar('DEPLOY_DIR_IPK'), pkg.split()[1], file_name)
                if os.path.isfile(file_full_path):
                    bb.utils.copyfile(file_full_path, os.path.join(incremental_ipk_dir, file_name))

def add_tarball_files() :
    bb.note("This is reserved for add tarball files")

python do_gen_incremental_pkg() {
    image_manifest = d.getVar('IMAGE_NAME') + d.getVar('IMAGE_NAME_SUFFIX') + '.manifest'
    bb.note("image_manifest:%s" % image_manifest)

    file1 = open(os.path.join(d.getVar('SDKBASEMETAPATH'), 'conf', image_manifest), 'r')
    manifest_ori = file1.readlines()
    file2 = open(d.getVar('IMAGE_MANIFEST'), 'r')
    manifest_inc = file2.readlines()
    file1.close()
    file2.close()

    #get defined incrimantal artifacts name
    Incrimantal_artifacts = d.getVar('INCRIMANTAL_ARTIFACTS') or 'incrimantal_artifacts'

    copy_incrimantal_ipk(manifest_ori,manifest_inc, d)

    #add tarball files, eg:scripts,documents,examples,install.sh,uninstall.sh
    add_tarball_files()

    #generate tarball,the tarball name is Incrimantal_artifacts
    import subprocess

    tarball_name = Incrimantal_artifacts + ".tar.gz"
    final_tarball_file = os.path.join(d.getVar('DEPLOY_DIR'), 'artifacts',tarball_name)
    tarball_file = os.path.join(d.getVar('DEPLOY_DIR'), tarball_name)
    artifacts_dir = os.path.join(d.getVar('DEPLOY_DIR'), 'artifacts')
    if os.path.exists(tarball_file):
        os.remove(tarball_file)
    if os.path.exists(final_tarball_file):
        os.remove(final_tarball_file)
    cmd = ['tar', '-czf', tarball_file, '-C', d.getVar('DEPLOY_DIR'), '--transform', 's/^artifacts/{}/'.format(Incrimantal_artifacts), 'artifacts']
    ret = subprocess.call(cmd)
    bb.utils.movefile(tarball_file,final_tarball_file)
    if ret != 0:
        bb.error('Failed to run %s!' % cmd)
}

addtask do_gen_incremental_pkg after do_rootfs before do_image
