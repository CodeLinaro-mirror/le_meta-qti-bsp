inherit core-image

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'avbtool-native', '', d)}"

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
        fi
    fi
}

addtask do_make_avb_image after do_image_complete before do_build

# create dummy vendor-boot & vbmeta image
# create dummy boot-init image for Andriod container
VENDORBOOT_IMG_CMD = " \
    dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/${VENDORBOOTIMAGE_TARGET} bs=1M count=48; \
    dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/${BOOTINIT_TARGET} bs=1K count=1; \
"

