SUMMARY = "Kernel Boot image"
DESCRIPTION = "Build kernel boot image"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "mkbootimg-native mkdtimg-native openssl-native  python3-native virtual/kernel"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'oot-dtbo', '', d)}"
DEPENDS += "${@bb.utils.contains_any('MACHINE', 'sa8797', bb.utils.contains('MACHINE_FEATURES', 'qti-multimedia', 'mm-vfio-devicetree', '', d), '', d)}"
DEPENDS += "${@bb.utils.contains('COMBINED_FEATURES', 'qti-audio-aw', 'audiolite-devicetree', '', d)}"
DEPENDS += "${@bb.utils.contains_any('COMBINED_FEATURES', 'qti-audio qti-audio-ar', bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah qti-umd', 'audiolite-devicetree', '', d), '', d)}"

IMAGE_CLASSES:remove = "qimage"
IMAGE_FEATURES:remove = "ssh-server-openssh"

DEPLOY_NAME_BASE_LAGVM = "${PRODUCT}-lagvm-automotive"
DEPLOY_NAME_LAGVM = "${DEPLOY_NAME_BASE_LAGVM}${@['-' + d.getVar('VARIANT', True), ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"
DEPLOY_DIR_IMAGE_LAGVM = "${DEPLOY_DIR}/images/${DEPLOY_NAME_LAGVM}"
DEPLOY_NAME_BASE_PVM = "${PRODUCT}-pvm-automotive"
DEPLOY_NAME_PVM = "${DEPLOY_NAME_BASE_PVM}${@['-' + d.getVar('VARIANT', True), ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"
DEPLOY_DIR_IMAGE_PVM = "${DEPLOY_DIR}/images/${DEPLOY_NAME_PVM}"

inherit image qcom-dtb-merge

EXTRA_IMAGE_FEATURES = ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

do_make_dtb() {
    install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs
    install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb

    if [ -d "${DEPLOY_DIR_IMAGE}/dtbs" ]; then
        rm -r ${DEPLOY_DIR_IMAGE}/dtbs
    fi
    install -d ${DEPLOY_DIR_IMAGE}/dtbs
    install -d ${DEPLOY_DIR_IMAGE}/dtbs/1gvm
    install -d ${DEPLOY_DIR_IMAGE}/dtbs/0gvm

    dtb_dir=${DEPLOY_DIR_IMAGE}/build-artifacts/dtb
    dtbo_dir=${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs
    out_dir=${DEPLOY_DIR_IMAGE}/dtbs

    merge_dtbos $dtb_dir $dtbo_dir $out_dir

    dtb_1gvm_files=$(find $out_dir -name "*1gvm*.dtb")
    if [ -n "$dtb_1gvm_files" ]; then
        mv $out_dir/*1gvm*.dtb ${DEPLOY_DIR_IMAGE}/dtbs/1gvm/
        cat ${DEPLOY_DIR_IMAGE}/dtbs/1gvm/*.dtb* > ${DEPLOY_DIR_IMAGE}/dtbs/1gvm/dtb.img
    fi

    dtb_0gvm_files=$(find $out_dir -name "*0gvm*.dtb")
    if [ -n "$dtb_0gvm_files" ]; then
        mv $out_dir/*0gvm*.dtb ${DEPLOY_DIR_IMAGE}/dtbs/0gvm/
        cat ${DEPLOY_DIR_IMAGE}/dtbs/0gvm/*.dtb* > ${DEPLOY_DIR_IMAGE}/dtbs/0gvm/dtb.img
    fi

    cat ${DEPLOY_DIR_IMAGE}/dtbs/*.dtb* > ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img
}
do_make_dtb[depends] += "virtual/kernel:do_deploy"
do_make_dtb[depends] += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'oot-dtbo:do_deploy', '', d)}"
do_make_dtb[depends] += "${@bb.utils.contains_any('MACHINE', 'sa8797', bb.utils.contains('MACHINE_FEATURES', 'qti-multimedia', 'mm-vfio-devicetree:do_deploy', '', d), '', d)}"
do_make_dtb[depends] += "${@bb.utils.contains('COMBINED_FEATURES', 'qti-audio-aw', 'audiolite-devicetree:do_deploy', '', d)}"
do_make_dtb[depends] += "${@bb.utils.contains_any('COMBINED_FEATURES', 'qti-audio qti-audio-ar', bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah qti-umd', 'audiolite-devicetree:do_deploy', '', d), '', d)}"

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
        if [ -f "${DEPLOY_DIR_IMAGE}/dtbs/1gvm/dtb.img" ]; then
            if [ ! -d "${DEPLOY_DIR_IMAGE_LAGVM}" ]; then
                install -d ${DEPLOY_DIR_IMAGE_LAGVM}
            fi
            # Make lagvm bootimage
            ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py --header_version ${KERNEL_IMAGE_HEADER_VERSION} \
            --kernel  ${DEPLOY_DIR_IMAGE}/Image \
            --dtb  ${DEPLOY_DIR_IMAGE}/dtbs/1gvm/dtb.img \
            --ramdisk ${BOOT_RAMDISK_IMG} \
            --pagesize ${PAGE_SIZE} \
            --base ${KERNEL_BASE} \
            --ramdisk_offset 0x0 \
            --cmdline "${KERNEL_CMD_PARAMS}" \
            --output  ${DEPLOY_DIR_IMAGE_LAGVM}/${PRODUCT}-lagvm-boot-${KERNEL_VERSION}.img
            cp ${DEPLOY_DIR_IMAGE_LAGVM}/${PRODUCT}-lagvm-boot-${KERNEL_VERSION}.img ${DEPLOY_DIR_IMAGE_LAGVM}/${PRODUCT}-lagvm-boot.img
        fi
        if [ -f "${DEPLOY_DIR_IMAGE}/dtbs/0gvm/dtb.img" ]; then
            if [ ! -d "${DEPLOY_DIR_IMAGE_PVM}" ]; then
                install -d ${DEPLOY_DIR_IMAGE_PVM}
            fi
            # Make pvm bootimage
            ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py --header_version ${KERNEL_IMAGE_HEADER_VERSION} \
            --kernel  ${DEPLOY_DIR_IMAGE}/Image \
            --dtb  ${DEPLOY_DIR_IMAGE}/dtbs/0gvm/dtb.img \
            --ramdisk ${BOOT_RAMDISK_IMG} \
            --pagesize ${PAGE_SIZE} \
            --base ${KERNEL_BASE} \
            --ramdisk_offset 0x0 \
            --cmdline "${KERNEL_CMD_PARAMS}" \
            --output  ${DEPLOY_DIR_IMAGE_PVM}/${PRODUCT}-pvm-boot-${KERNEL_VERSION}.img
            cp ${DEPLOY_DIR_IMAGE_PVM}/${PRODUCT}-pvm-boot-${KERNEL_VERSION}.img ${DEPLOY_DIR_IMAGE_PVM}/${PRODUCT}-pvm-boot.img
        fi
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

#sign boot img
do_sign_boot_img () {
    imgname="${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET}"
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'true', 'false', d)}; then
        avb_sign_boot_image ${imgname}
        if [ -f "${DEPLOY_DIR_IMAGE_LAGVM}/${PRODUCT}-lagvm-boot.img" ]; then
            imgname="${DEPLOY_DIR_IMAGE_LAGVM}/${PRODUCT}-lagvm-boot.img"
            avb_sign_boot_image ${imgname}
        fi
        if [ -f "${DEPLOY_DIR_IMAGE_PVM}/${PRODUCT}-pvm-boot.img" ]; then
            imgname="${DEPLOY_DIR_IMAGE_PVM}/${PRODUCT}-pvm-boot.img"
            avb_sign_boot_image ${imgname}
        fi
    fi
}

avb_sign_boot_image() {
    img="$1"
    boot_partition_size=$(avbtool calc_min_partition_size \
                              --image ${img} \
                              --partition_name boot \
                              --hash_algorithm sha256 \
                              --no_hashtree)
    avbtool add_hash_footer  \
        --image ${img}  \
        --partition_size ${boot_partition_size}  \
        --partition_name boot \
        --algorithm SHA256_RSA4096 \
        --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
        --rollback_index 0
    if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
       avbtool add_hash_footer  \
              --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img  \
              --partition_size 0x00200000 \
              --partition_name dtbo \
              --algorithm SHA256_RSA4096 \
              --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
              --rollback_index 0
    fi
}

#Sign boot image after generation
do_sign_boot_img[dirs] = "${DEPLOY_DIR_IMAGE}"
do_sign_boot_img[depends] += "sectools-native:do_populate_sysroot avbtool-native:do_populate_sysroot"

addtask do_makeboot_setscene

addtask do_makeboot before do_build
addtask do_sign_boot_img after do_makeboot before do_build

do_rootfs[noexec] = "1"
do_image[noexec] = "1"
do_image_complete[noexec] = "1"
