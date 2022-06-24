#Copyright (c) 2017, 2019-2022 Qualcomm Innovation Center, Inc. All rights reserved.

#Redistribution and use in source and binary forms, with or without
#modification, are permitted (subject to the limitations in the
#disclaimer below) provided that the following conditions are met:
#
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#
#     * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#
#    * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.
#
#NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE
#GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
#HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
#WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
#MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
#IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
#ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
#DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
#GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
#INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
#IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
#OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
#IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#
#Changes from Qualcomm Innovation Center are provided under the following license:
#
#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

inherit populate_sdk_ext

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
        f.write('WORKSPACE = "$' + '{TOPDIR}/src"\n')
        f.write('WORKSPACEROOT = "$' + '{TOPDIR}/"\n')
    with open(baseoutpath + '/conf/local.conf', 'a') as f:
        f.write('\nPREBUILT_SRC_DIR = "%s"\n' % d.getVar('PREBUILT_SRC_DIR'))

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
