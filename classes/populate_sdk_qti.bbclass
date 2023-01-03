# Copyright (c) 2021, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
# BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
# BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
# OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

# The majority of populate_sdk is located in populate_sdk_base
# which is inherited by populate_sdk_ext. So inheriting
# populate_sdk_ext also helps to run populate_sdk task.

inherit populate_sdk_ext

python copy_buildsystem_append() {
    #copy image_manifest to esdk, which record the packages installed in image
    image_manifest = d.getVar('IMAGE_MANIFEST')
    image_name = d.getVar('IMAGE_NAME')
    image_suffix = d.getVar('IMAGE_NAME_SUFFIX')
    shutil.copyfile(image_manifest, baseoutpath + '/conf/' + image_name + image_suffix + '.manifest')
}

def create_filtered_tasklist(d, sdkbasepath, tasklistfile, conf_initpath):
    """
    Create a filtered list of tasks. Also double-checks that the build system
    within the SDK basically works and required sstate artifacts are available.
    """
    import tempfile
    import shutil
    import oe.copy_buildsystem

    # Enable the use of WORKSPACE variable on an extensible SDK
    baseoutpath = d.getVar('SDK_OUTPUT') + '/' + d.getVar('SDKPATH')
    with open(baseoutpath + '/conf/bblayers.conf', 'a') as f:
        f.write('WORKSPACE = "$' + '{TOPDIR}/layers/src"\n')
        f.write('WORKSPACEROOT = "$' + '{TOPDIR}/layers/"\n')
    with open(baseoutpath + '/conf/local.conf', 'a') as f:
        f.write('\nPREBUILT_SRC_DIR = "%s"\n' % d.getVar('PREBUILT_SRC_DIR'))
    #Copy HY11 prebuilt tar.gz to extensible SDK
    src_prebuilt_hy11 = os.path.abspath(d.getVar('WORKSPACEROOT') + '/prebuilt_HY11')
    dest_prebuilt_hy11 = os.path.join(baseoutpath,'prebuilt_HY11')
    shutil.copytree(src_prebuilt_hy11,dest_prebuilt_hy11)

    # Create a temporary build directory that we can pass to the env setup script
    shutil.copyfile(sdkbasepath + '/conf/local.conf', sdkbasepath + '/conf/local.conf.bak')
    try:
        with open(sdkbasepath + '/conf/local.conf', 'a') as f:
            # Force the use of sstate from the build system
            f.write('\nSSTATE_DIR_forcevariable = "%s"\n' % d.getVar('SSTATE_DIR'))
            f.write('SSTATE_MIRRORS_forcevariable = "file://universal/(.*) file://universal-4.9/\\1 file://universal-4.9/(.*) file://universal-4.8/\\1"\n')
            # Ensure TMPDIR is the default so that clean_esdk_builddir() can delete it
            f.write('TMPDIR_forcevariable = "${TOPDIR}/tmp"\n')
            f.write('TCLIBCAPPEND_forcevariable = ""\n')
            # Drop uninative if the build isn't using it (or else NATIVELSBSTRING will
            # be different and we won't be able to find our native sstate)
            if not bb.data.inherits_class('uninative', d):
                f.write('INHERIT_remove = "uninative"\n')

        # Unfortunately the default SDKPATH (or even a custom value) may contain characters that bitbake
        # will not allow in its COREBASE path, so we need to rename the directory temporarily
        temp_sdkbasepath = d.getVar('SDK_OUTPUT') + '/tmp-renamed-sdk'
        # Delete any existing temp dir
        try:
            shutil.rmtree(temp_sdkbasepath)
        except FileNotFoundError:
            pass
        os.rename(sdkbasepath, temp_sdkbasepath)
        cmdprefix = '. %s .; ' % conf_initpath
        logfile = d.getVar('WORKDIR') + '/tasklist_bb_log.txt'
        try:
            oe.copy_buildsystem.check_sstate_task_list(d, get_sdk_install_targets(d), tasklistfile, cmdprefix=cmdprefix, cwd=temp_sdkbasepath, logfile=logfile)
        except bb.process.ExecutionError as e:
            msg = 'Failed to generate filtered task list for extensible SDK:\n%s' %  e.stdout.rstrip()
            if 'attempted to execute unexpectedly and should have been setscened' in e.stdout:
                msg += '\n----------\n\nNOTE: "attempted to execute unexpectedly and should have been setscened" errors indicate this may be caused by missing sstate artifacts that were likely produced in earlier builds, but have been subsequently deleted for some reason.\n'
            bb.fatal(msg)
        os.rename(temp_sdkbasepath, sdkbasepath)
        # Clean out residue of running bitbake, which check_sstate_task_list()
        # will effectively do
        clean_esdk_builddir(d, sdkbasepath)
    finally:
        os.replace(sdkbasepath + '/conf/local.conf.bak', sdkbasepath + '/conf/local.conf')




# To include protoc compiler in SDK
TOOLCHAIN_HOST_TASK_append = " nativesdk-protobuf-compiler "

# Add nativesdk-llvm-arm-toolchain in SDK to run on SDKMACHINE
TOOLCHAIN_HOST_TASK_append = " nativesdk-llvm-arm-toolchain"

# To include kernel headers in SDK
TOOLCHAIN_TARGET_TASK_append = " linux-msm-headers-dev"

# To include kernel sources in SDK to build kernel modules
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"

TOOLCHAIN_TARGET_TASK_append = "  ath6kl-utils-staticdev"
