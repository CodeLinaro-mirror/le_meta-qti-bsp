inherit core-image dm-verity

IMAGE_FEATURES[validitems] += "sparse-image"

DEPENDS += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'avbtool-native', '', d)} \
    ${@bb.utils.contains('IMAGE_FEATURES', 'sparse-image', 'libsparse-native', '', d)} \
"

# Make sparse rootfs by default
create_sparsesystem() {
    mv ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ext4 ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tmp
    img2simg ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tmp ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ext4
    rm -f ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tmp
}

do_image_ext4[postfuncs] += "${@bb.utils.contains('IMAGE_FEATURES', 'sparse-image', 'create_sparsesystem', '', d)}"

### Generate system.img #####
# Alter system image size if varity is enabled.
do_makesystem[prefuncs]  += " ${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', 'adjust_system_size_for_verity', '', d)}"
do_makesystem[postfuncs] += " ${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', 'make_verity_enabled_system_image', '', d)}"
do_makesystem[dirs]       = "${DEPLOY_DIR_IMAGE}"

# With dm-verity, kernel cmdline has to be updated with correct hash value of
# system image. This means final boot image can be created only after system image.
# But many a times when only kernel need to be built waiting for full image is
# time consuming. To over come this make_veritybootimg task is added to build boot
# img with verity. Normal do_make_bootimg continue to build boot.img without verity.
python do_make_veritybootimg () {
    import subprocess

    xtra_parms=""
    if bb.utils.contains('DISTRO_FEATURES', 'nand-boot', True, False, d):
        xtra_parms = " --tags-addr" + " " + d.getVar('KERNEL_TAGS_OFFSET')

    verity_cmdline = ""
    if bb.utils.contains('DISTRO_FEATURES', 'dm-verity', True, False, d):
        verity_cmdline = get_verity_cmdline(d).strip()

    mkboot_bin_path = d.getVar('STAGING_BINDIR_NATIVE', True) + '/mkbootimg'
    zimg_path       = d.getVar('DEPLOY_DIR_IMAGE', True) + "/" + d.getVar('KERNEL_IMAGETYPE', True)
    cmdline         = "\"" + d.getVar('KERNEL_CMD_PARAMS', True) + " " + verity_cmdline + "\""
    pagesize        = d.getVar('PAGE_SIZE', True)
    base            = d.getVar('KERNEL_BASE', True)
    output          = d.getVar('DEPLOY_DIR_IMAGE', True) + "/" + d.getVar('BOOTIMAGE_TARGET', True)

    # cmd to make boot.img
    cmd =  mkboot_bin_path + " --kernel %s --cmdline %s --pagesize %s --base %s %s --ramdisk /dev/null --ramdisk_offset 0x0 --output %s" \
           % (zimg_path, cmdline, pagesize, base, xtra_parms, output )

    bb.debug(1, "do_make_veritybootimg cmd: %s" % (cmd))

    subprocess.call(cmd, shell=True)
}
do_make_veritybootimg[depends]  += "${PN}:do_makesystem"
do_make_veritybootimg[dirs]      = "${DEPLOY_DIR_IMAGE}"

python () {
    if bb.utils.contains('DISTRO_FEATURES', 'dm-verity', True, False, d):
        bb.build.addtask('do_make_veritybootimg', 'do_image_complete', 'do_rootfs', d)
}

do_make_avb_image(){
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'true', 'false', d)}; then
        # In qti-image-headless.bb, need to set IMAGE_ROOTFS_ZIZE to 700M, set the value of the judgment of 500M to skip redefinition.
        if [[ "${IMAGE_ROOTFS_SIZE}" -lt "524288" ]]; then
            # core minimal image define the IMAGE_ROOTFS_SIZE to 8192, no way to calculate
            # an appropriate partition size for hashtree footer on top of rootfs image.
            rootfs_size_kb=1572864
        else
            rootfs_size_kb=${IMAGE_ROOTFS_SIZE}
        fi

        rootfs_size=$(expr $rootfs_size_kb \* 1024)
        overhead_size_kb=$(expr $rootfs_size_kb / 5)
        overhead_size=$(expr $overhead_size_kb \* 1024)

        if [ "$(expr $overhead_size % 4096)" != "0" ]; then
            overhead_size=$(expr $(expr 4096 - $(expr $overhead_size % 4096)) + $overhead_size)
        fi
        rootfs_partition_size=$(expr $rootfs_size + $overhead_size)

        if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
            #For lvgvm avb2.0, add hashtree for system image and generate vbmeta.img.
            avbtool add_hashtree_footer \
                --image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
                --partition_name system \
                --partition_size ${rootfs_partition_size} \
                --hash_algorithm sha256 \
                --do_not_generate_fec
            avbtool make_vbmeta_image \
	        --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} \
	        --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
	        --setup_rootfs_from_kernel ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
                --algorithm SHA256_RSA4096 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/vbgvm_private_key_4096.pem \
                --rollback_index 0 \
                --output ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-vbmeta.img
            # Workaround, to keep two vbmeta images here with different vbmeta name.
            install -m 644 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-vbmeta.img ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vbmeta.img
            install -m 644 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-vbmeta.img ${DEPLOY_DIR_IMAGE}/vbmeta.img
        else
            #For lv avb2.0, add hashtree for system image and generate vbmeta.img.
            avbtool add_hashtree_footer \
                --image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
                --partition_size ${rootfs_partition_size} \
                --partition_name system  \
                --hash_algorithm sha256 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                --rollback_index 0 \
                --do_not_generate_fec
            avbtool make_vbmeta_image \
                --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} \
                --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img \
                --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
                --setup_rootfs_from_kernel ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
                --algorithm SHA256_RSA4096 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                --rollback_index 0 \
                --output ${DEPLOY_DIR_IMAGE}/${VBMETAIMAGE_TARGET}
        fi
    fi
}

addtask do_make_avb_image after do_image_complete before do_build


VENDORBOOT_IMG_CMD = " \
    # create dummy vendor-boot & vbmeta image
    dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/${VENDORBOOTIMAGE_TARGET} bs=1M count=96; \
    dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/${VBMETAIMAGE_TARGET} bs=1K count=3; \

    # create dummy boot-init image for Andriod container
    dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/${BOOTINIT_TARGET} bs=1K count=1; \
"

# compress the image to ease the upload
IMAGE_CMD:ext4:append:sa81x5 = "; \
  ${VENDORBOOT_IMG_CMD} \
"
