SUMMARY = "QTI ARK Kernel Boot image"
DESCRIPTION = "Build QTI ARK kernel boot image"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "mkbootimg-native mkdtimg-native openssl-native oot-dtbo python3-native virtual/kernel"

IMAGE_CLASSES:remove = "qimage"
IMAGE_FEATURES:remove = "ssh-server-openssh"

inherit image

EXTRA_IMAGE_FEATURES = ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

do_make_dtb() {
     cat ${DEPLOY_DIR_IMAGE}/dtbs/*.dtb* > ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img
}
do_make_dtb[depends] += "oot-dtbo:do_deploy"

addtask do_make_dtb after do_image before do_makeboot

BOOT_RAMDISK_IMG ?= "${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'early-ramdisk-image-${PRODUCT}.cpio.lz4', '/dev/null', d)}"

do_makeboot () {
    if [ "${KERNEL_IMAGE_HEADER_VERSION}" = "2" ]; then
        # Make bootimage
        ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py --header_version ${KERNEL_IMAGE_HEADER_VERSION} \
        --kernel  ${DEPLOY_DIR_IMAGE}/Image \
        --dtb  ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img \
        --ramdisk ${BOOT_RAMDISK_IMG} \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --output  ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot-${KERNEL_VERSION}.img
    elif [ "${KERNEL_IMAGE_HEADER_VERSION}" = "1" ]; then
        # Make bootimage
        ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py \
        --kernel  ${DEPLOY_DIR_IMAGE}/Image.gz-dtb \
        --ramdisk ${BOOT_RAMDISK_IMG} \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --output  ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot-${KERNEL_VERSION}.img
    else
        echo "Unknown Boot Image Header Version"
        return 1
    fi
    cp ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot-${KERNEL_VERSION}.img ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot.img
}

do_makeboot[dirs] = "${DEPLOY_DIR_IMAGE}"
# Make sure native tools and vmlinux ready to create boot.img
do_makeboot[depends] += "virtual/kernel:do_deploy mkbootimg-native:do_populate_sysroot"
do_makeboot[depends] += "${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'early-ramdisk-image:do_image_complete', ' ', d)}"
do_makeboot[sstate-inputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[stamp-extra-info] = "${MACHINE_ARCH}"

python do_makeboot_setscene () {
    sstate_setscene(d)
}

addtask do_makeboot_setscene

addtask do_makeboot before do_build

do_rootfs[noexec] = "1"
do_image[noexec] = "1"
do_image_complete[noexec] = "1"
