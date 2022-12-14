#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

do_merge_dtbs() {
     install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos
     cd ${SRC_DIR_ROOT}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform && \
     LD_LIBRARY_PATH=../out/msm-kernel-${KERNEL_ARCH}-${KERNEL_VARIANT}defconfig/host/lib/:$LD_LIBRARY_PATH \
     OUT_DIR=${SRC_DIR_ROOT}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/out/msm-kernel-${KERNEL_ARCH}-${KERNEL_VARIANT}defconfig/ \
     BUILD_CONFIG=${KERNEL_BUILD_CONFIG}  \
     ./build/android/merge_dtbs.sh \
     ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb \
     ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos ${DEPLOY_DIR_IMAGE}/dtbs
}

addtask do_merge_dtbs after do_image before do_makeboot

MKBOOTUTIL = '${@oe.utils.conditional("PREFERRED_PROVIDER_mkbootimg-native", "mkbootimg-gki-native", "scripts/mkbootimg.py", "mkbootimg", d)}'

python do_makeboot () {
    import subprocess

    mkboot_bin_path = d.getVar('STAGING_BINDIR_NATIVE', True) + "/" + d.getVar('MKBOOTUTIL')

    kernel_path = d.getVar('DEPLOY_DIR_IMAGE', True) + "/" + d.getVar('KERNEL_IMAGETYPE', True)
    dtb_path = d.getVar('DEPLOY_DIR_IMAGE', True) + "/dtbs/dtb.img"
    header_version = d.getVar('KERNEL_IMAGE_HEADER_VERSION', True)
    cmdline = "\"" + d.getVar('KERNEL_CMD_PARAMS', True) + "\""
    pagesize = d.getVar('PAGE_SIZE', True)
    base = d.getVar('KERNEL_BASE', True)
    output = d.getVar('BOOTIMAGE_TARGET', True)

    # cmd to make boot.img
    cmd = mkboot_bin_path + " --kernel %s --dtb %s --cmdline %s --pagesize %s --base %s --header_version %s --ramdisk /dev/null --output %s" \
        % (kernel_path, dtb_path, cmdline, pagesize, base, header_version, output )

    bb.debug(1, "do_makeboot cmd: %s" % (cmd))
    try:
        ret = subprocess.check_output(cmd, shell=True)
    except RuntimeError as e:
         bb.error("do_makeboot cmd: %s failed with error %s" % (cmd, str(e)))
}

do_makeboot[dirs] = "${DEPLOY_DIR_IMAGE}"
# Make sure native tools and vmlinux ready to create boot.img
do_makeboot[depends] += "virtual/kernel:do_deploy mkbootimg-native:do_populate_sysroot"
do_makeboot[sstate-inputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[stamp-extra-info] = "${MACHINE_ARCH}"

python do_makeboot_setscene () {
    sstate_setscene(d)
}

#sign boot, dtbo and vendor-boot img
do_sign_boot_img () {
    imgname="${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET}"
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'false', 'true', d)}; then
       if ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "true", "false", d)}; then
          if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'true', 'false', d)}; then
             avb_sign_boot_image ${imgname}
          fi
       fi
    fi
}

avb_sign_boot_image() {
        img="$1"
        # For lv avb2.0, add hash for boot image, dtbo image and vendor-boot image.
        avbtool add_hash_footer  \
            --image ${img}  \
            --partition_size 0x04000000  \
            --partition_name boot \
            --algorithm SHA256_RSA4096 \
            --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
            --rollback_index 0
        if [ -f ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img ]; then
            avbtool add_hash_footer  \
                --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img  \
                --partition_size 0x04000000  \
                --partition_name vendor_boot \
                --algorithm SHA256_RSA4096 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                --rollback_index 0
        fi
        avbtool add_hash_footer  \
            --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img  \
            --partition_size 0x00200000 \
            --partition_name dtbo \
            --algorithm SHA256_RSA4096 \
            --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
            --rollback_index 0
}
#Sign boot image after generation
do_sign_boot_img[dirs] = "${DEPLOYDIR}"

addtask do_makeboot_setscene

addtask do_makeboot before do_image_complete
addtask do_sign_boot_img after do_image_complete before do_make_avb_image
