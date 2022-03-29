SUMMARY = "Stage prebuilt linux kernel artifacts"
DESCRIPTION = "Installs prebuilt linux kernel artifacts to STAGING_KERNEL_BUILDDIR"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://kernel_platform/msm-kernel/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit native

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   =  "file://kernel-5.10"

S = "${WORKDIR}/kernel-5.10"

COPY_DIRECTORY_TREE = "${COREBASE}/meta-qti-bsp/files/copy_directory_tree.sh"

do_configure() {
    # Remove artifacts that are not required for building out of tree kernel modules and dtbo's
    cd ${WORKDIR}/kernel-5.10/kernel_platform
    mkdir -p ${WORKDIR}/kernel-5.10/kernel_platform_ship/prebuilts
    mv -nf build common msm-kernel prebuilts-master \
       -t ${WORKDIR}/kernel-5.10/kernel_platform_ship
    mv -nf prebuilts/build-tools prebuilts/gcc \
       -t ${WORKDIR}/kernel-5.10/kernel_platform_ship/prebuilts
}

do_install() {
    # Stage kernel artifacts to STAGING_KERNEL_DIR to build out of tree kernel modules and dtbo's
    mkdir -p ${STAGING_KERNEL_DIR}/kernel-5.10/kernel_platform
    ${COPY_DIRECTORY_TREE} ${WORKDIR}/kernel-5.10/kernel_platform_ship ${STAGING_KERNEL_DIR}/kernel-5.10/kernel_platform

    # Stage kernel defconfig to STAGING_KERNEL_DIR
    mkdir -p ${STAGING_KERNEL_DIR}/kernel-5.10/out/${KERNEL_DEFCONFIG}
    ${COPY_DIRECTORY_TREE} ${WORKDIR}/kernel-5.10/out/${KERNEL_DEFCONFIG} ${STAGING_KERNEL_DIR}/kernel-5.10/out/${KERNEL_DEFCONFIG}
}
