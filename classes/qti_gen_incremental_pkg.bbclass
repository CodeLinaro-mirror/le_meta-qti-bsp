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


python do_gen_incremental_pkg() {
    # These package suffixes are taken from the definitions of
    # PACKAGES and PACKAGES_DYNAMIC in meta/conf/bitbake.conf
    pn_suffixs = ['', '-dbg', '-dev', '-doc', '-staticdev', '-locale']
    image_manifest = d.getVar('IMAGE_NAME') + d.getVar('IMAGE_NAME_SUFFIX') + '.manifest'
    bb.note("image_manifest:%s" % image_manifest)

    file1 = open(os.path.join(d.getVar('SDKBASEMETAPATH'), 'conf', image_manifest), 'r')
    manifest_ori = file1.readlines()
    file2 = open(d.getVar('IMAGE_MANIFEST'), 'r')
    manifest_inc = file2.readlines()
    file1.close()
    file2.close()

    incremental_pkgs = find_incremental_pkg(manifest_ori, manifest_inc)

    incremental_ipk_dir = os.path.join(d.getVar('DEPLOY_DIR'), 'artifacts', 'incremental_ipk')
    bb.note("incremental_ipk_dir: %s" % incremental_ipk_dir)
    if os.path.exists(incremental_ipk_dir):
        oe.path.remove(incremental_ipk_dir)
    bb.utils.mkdirhier(incremental_ipk_dir)

    for pkg in incremental_pkgs:
        if len(pkg.split()):
            for suffix in pn_suffixs:
                # PF: Specifies the recipe or package name and includes all version and revision numbers
                # (i.e. glibc-2.13-r20+svnr15508/ and bash-4.2-r1/). This variable is comprised of the
                # following: ${PN}-${EXTENDPE}${PV}-${PR}
                file_name = pkg.split()[0] + suffix + '_' + pkg.split()[2].split(":", 1)[-1] + '_' + pkg.split()[1] + '.ipk'
                file_full_path = os.path.join(d.getVar('DEPLOY_DIR_IPK'), pkg.split()[1], file_name)
                if os.path.isfile(file_full_path):
                    bb.utils.copyfile(file_full_path, os.path.join(incremental_ipk_dir, file_name))

    import subprocess

    tarball_file = os.path.join(d.getVar('DEPLOY_DIR'), 'artifacts', 'incremental_ipk.tar.gz')
    if os.path.exists(tarball_file):
        os.remove(tarball_file)
    ret = subprocess.call(['tar', '-czf', tarball_file, incremental_ipk_dir])
    if ret != 0:
        bb.error('Failed to run %s!' % cmd)
}

addtask do_gen_incremental_pkg after do_rootfs before do_image
