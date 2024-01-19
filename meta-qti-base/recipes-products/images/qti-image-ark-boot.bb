SUMMARY = "QTI ARK Kernel Boot image"
DESCRIPTION = "Build QTI ARK kernel boot image"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "mkbootimg-native mkdtimg-native openssl-native python3-native virtual/kernel"

IMAGE_CLASSES:remove = "qimage"
IMAGE_FEATURES:remove = "ssh-server-openssh"

inherit image

EXTRA_IMAGE_FEATURES = ""

BOOT_RAMDISK_IMG ?= "${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'early-ramdisk-image-${PRODUCT}.cpio.lz4', '/dev/null', d)}"

BOOT_RAMDISK_CMD ?= "${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'rdinit=/sbin/early-ramdisk-init early-ramdisk.mode=0', '', d)}"

CONSOLE_CMD = "${@bb.utils.contains('PERF_BUILD', '1', '', 'console=ttyMSM0,115200,n8 earlycon=qcom_geni,0xa8c000' , d)}"

do_makeboot () {
    if [ "${BASEMACHINE}" = "sa8775" ]; then
        # Make bootimage
        ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py --header_version ${KERNEL_IMAGE_HEADER_VERSION} \
        --kernel  ${DEPLOY_DIR_IMAGE}/Image \
        --dtb  ${DEPLOY_DIR_IMAGE}/sa8775p-ride.dtb.overlay \
        --ramdisk ${BOOT_RAMDISK_IMG} \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        --cmdline "${BOOT_RAMDISK_CMD} ${CONSOLE_CMD} rw rootwait no_console_suspend=1 androidboot.hardware=qcom androidboot.console=ttyMSM0 lpm_levels.sleep_disabled=1 msm_rtb.filter=0x237 fips=0 notests nokaslr ignore_loglevel firmware_class.path=/firmware/vm/boot systemd.gpt_auto=0" \
        --output  ${DEPLOY_DIR_IMAGE}/sa8775p-boot-5.14.img
        cp ${DEPLOY_DIR_IMAGE}/sa8775p-boot-5.14.img ${DEPLOY_DIR_IMAGE}/sa8775-boot.img
    else
        # Make bootimage
        ${STAGING_BINDIR_NATIVE}/mkbootimg --kernel ${D}/${KERNEL_IMAGEDEST}/Image.gz-dtb \
        --kernel  ${DEPLOY_DIR_IMAGE}/Image.gz-dtb \
        --ramdisk ${BOOT_RAMDISK_IMG} \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        --cmdline "${BOOT_RAMDISK_CMD} rw rootwait console=ttyMSM0,115200,n8 no_console_suspend=1 androidboot.hardware=qcom androidboot.console=ttyMSM0 lpm_levels.sleep_disabled=1 msm_rtb.filter=0x237 earlycon=qcom_geni,0x884000 fips=0 notests nokaslr ignore_loglevel firmware_class.path=/firmware " \
        --output  ${DEPLOY_DIR_IMAGE}/sa8540p-boot-5.14.img
    fi
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
