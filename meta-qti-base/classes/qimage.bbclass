inherit core-image dm-verity

DEPENDS += "avbtool-native"

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

do_make_dm_verity_avb2_image(){
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        rootfs_size_kb=${IMAGE_ROOTFS_SIZE}
        rootfs_size=$(expr $rootfs_size_kb \* 1024)
        overhead_size_kb=$(expr $rootfs_size_kb / 5)
        overhead_size=$(expr $overhead_size_kb \* 1024)

        if [ "$(expr $size_bytes % 4096)" != "0" ]; then
            overhead_size=$(expr $(expr 4096 - $(expr $overhead_size % 4096)) + $overhead_size)
        fi

        rootfs_partition_size=$(expr $rootfs_size + $overhead_size)

        avbtool add_hash_footer --image ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} --partition_size 0x04000000 --partition_name boot \
        --algorithm SHA256_RSA4096 \
        --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/vbgvm_private_key_4096.pem --rollback_index 0
        avbtool add_hashtree_footer --image ${DEPLOY_DIR_IMAGE}/machine-image-${PRODUCT}.ext4 --partition_name system --partition_size ${rootfs_partition_size} --hash_algorithm sha256 --do_not_generate_fec
        avbtool make_vbmeta_image \
	--include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} \
	--include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
	--setup_rootfs_from_kernel ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 \
        --algorithm SHA256_RSA4096 \
        --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/vbgvm_private_key_4096.pem --rollback_index 0 --output ${DEPLOY_DIR_IMAGE}/vbmeta.img
    fi
}

addtask do_make_dm_verity_avb2_image after do_image_complete before do_build
