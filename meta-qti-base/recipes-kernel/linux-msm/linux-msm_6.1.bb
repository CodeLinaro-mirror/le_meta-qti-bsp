SUMMARY = "CLO Linux Kernel"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

require recipes-kernel/linux-msm/linux-msm.inc

COMPATIBLE_MACHINE = "monaco|quin-gvm-lemans"

SRC_URI = "${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/msm-kernel/.git;protocol=${PROTO};destsuffix=kernel/kernel-${PV}/kernel_platform/msm-kernel;usehead=1"
SRC_URI:append= " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-lvumd', 'file://lvumd.cfg', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-lvumd', 'file://lvumd', '', d)} \
"

do_patch:append() {
    PATCH_LIST=`ls ${WORKDIR}/lvumd/*.patch`
    for PATCH_FILE in ${PATCH_LIST}; do
        patch -f -p1 < ${PATCH_FILE}
        if [ $? != 0 ];then
            bbfatal "patching ${PATCH_FILE} failed"
        fi
    done
}

S = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/msm-kernel"
