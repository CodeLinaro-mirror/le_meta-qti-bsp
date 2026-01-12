DEPENDS += "cryptsetup-native openssl-native"

CONFLICT_MACHINE_FEATURES += " dm-verity-bootloader dm-verity-none"

CORE_IMAGE_EXTRA_INSTALL += "cryptsetup"

VERITY_SALT = "aee087a5be3b982978c923f566a94613496b417f2af592639bc80d141e34dfe7"
BLOCK_SIZE = "4096"
FEC_ROOTS = "2"

VERITY_HASH_DEVICE = "${WORKDIR}/system.verityhash"
VERITY_FEC_DEVICE = "${WORKDIR}/system.verityfec"
UNSPARSED_SYSTEMIMAGE = "${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${SYSTEMIMAGE_TARGET}"

populate_verity_env () {
    # read previously generated values from system_verity_metadata.txt
    #  and copy into /etc/verity.env inside the ramdisk filesystem.
    data_blocks=`awk -F ':' '{ if ($1 == "Data blocks") print $2 }' ${WORKDIR}/system_verity_metadata.txt | sed "s/^[ \t]*//"`
    hash_offset=`expr $data_blocks \* 4096`
    fec_offset=`awk -F ':' '{ if ($1 == "fec_offset") print $2 }' ${WORKDIR}/system_verity_metadata.txt | sed "s/^[ \t]*//"`
    root_hash=`awk -F ':' '{ if ($1 == "Root hash") print $2 }' ${WORKDIR}/system_verity_metadata.txt | sed "s/^[ \t]*//"`
    cat <<-EOF > ${WORKDIR}/verity.env
	VERITY_DATA_BLOCKS=${data_blocks}
	VERITY_HASH_OFFSET=${hash_offset}
	VERITY_FEC_OFFSET=${fec_offset}
	VERITY_FEC_ROOTS=${FEC_ROOTS}
	VERITY_SALT=${VERITY_SALT}
	VERITY_ROOT_HASH=${root_hash}
	EOF

    # Sign the root hash
    echo -n "${root_hash}" > ${WORKDIR}/roothash.txt
    openssl smime -sign -nocerts -noattr -binary -in ${WORKDIR}/roothash.txt -inkey ${KERNEL_PREBUILT_PATH}/dist/verity_key.pem -signer ${KERNEL_PREBUILT_PATH}/dist/verity_cert.pem -outform der -out ${WORKDIR}/verity_sig.txt
}
do_makesystem[postfuncs] += "populate_verity_env"

# ramdisk creation now requires the verity artifacts
do_ramdisk_create[depends] += "${PN}:do_makesystem"

##### Generate boot.img ######
BOOTIMGDEPLOYDIR = "${WORKDIR}/deploy-${PN}-bootimage-complete"

INITRAMFS_IMAGE ?= ''
RAMDISK ?= "${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.${INITRAMFS_FSTYPES}"
VRAMDISK = "${DEPLOY_DIR_IMAGE}/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.${INITRAMFS_FSTYPES}"

def get_ramdisk_path(d):
    if os.path.exists(d.getVar('RAMDISK')):
        return '%s' %(d.getVar('RAMDISK'))
    return '/dev/null'

RAMDISK_PATH = "${@get_ramdisk_path(d)}"

MKBOOTUTIL = '${@oe.utils.conditional("PREFERRED_PROVIDER_virtual/mkbootimg", "mkbootimg-gki", "scripts/mkbootimg.py", "mkbootimg", d)}'

# If BOOT_HEADER_VERSION >= 3, a vendor_boot image will be built
#  unless SKIP_VENDOR_BOOT is defined as True.
python do_makeboot () {
    import subprocess

    # Set cmdline
    cmdline=""
    if ((int(d.getVar("BOOT_HEADER_VERSION") or "0") < 3) or (d.getVar("SKIP_VENDOR_BOOT") or "True") == "True"):
        cmdline = " --cmdline" + "\"" + d.getVar('KERNEL_CMD_PARAMS', True) + "\""
    else:
        cmdline     = " --vendor_cmdline " + "\"" + d.getVar('KERNEL_CMD_PARAMS', True) + "\""

    xtra_parms=""
    if bb.utils.contains('MACHINE_FEATURES', 'nand-boot', True, False, d):
        xtra_parms = " --tags-addr" + " " + d.getVar('KERNEL_TAGS_OFFSET')
    if (int(d.getVar("BOOT_HEADER_VERSION") or "0") >= 2):
        xtra_parms += " --header_version " + d.getVar('BOOT_HEADER_VERSION')
        # header version setting expects dtb to be passed seprately but not appended to kernel
        xtra_parms += " --dtb " + d.getVar('DEPLOY_DIR_IMAGE', True) + "/DTOverlays" + "/dtb.img"

    if ((int(d.getVar("BOOT_HEADER_VERSION") or "0") >= 3) and (d.getVar("SKIP_VENDOR_BOOT") or "True") == "False"):
        xtra_parms += " --vendor_ramdisk %s" %(d.getVar('VRAMDISK'))
        xtra_parms += " --vendor_boot " + d.getVar('VBOOTIMAGE_TARGET')

    mkboot_bin_path = d.getVar('STAGING_BINDIR_NATIVE', True) + "/" + d.getVar('MKBOOTUTIL')
    ramdisk_path    = d.getVar('RAMDISK_PATH')
    zimg_path       = d.getVar('DEPLOY_DIR_IMAGE', True) + "/" + d.getVar('KERNEL_IMAGETYPE', True)
    pagesize        = d.getVar('PAGE_SIZE', True)
    base            = d.getVar('KERNEL_BASE', True)
    output          = d.getVar('BOOTIMAGE_TARGET', True)

    # cmd to make boot.img
    cmd =  mkboot_bin_path + " --kernel %s %s --pagesize %s --base %s --ramdisk %s --ramdisk_offset 0x0 %s --output %s" \
           % (zimg_path, cmdline, pagesize, base, ramdisk_path, xtra_parms, output )
    bb.debug(1, "dm-verity-initramfs do_makeboot cmd: %s" % (cmd))
    try:
        ret = subprocess.check_output(cmd, shell=True)
    except RuntimeError as e:
        bb.error("dm-verity-initramfs cmd: %s failed with error %s" % (cmd, str(e)))

}
do_makeboot[dirs]      = "${BOOTIMGDEPLOYDIR}/${IMAGE_BASENAME}"
# Make sure native tools and vmlinux ready to create boot.img
do_makeboot[depends] += "virtual/kernel:do_deploy virtual/mkbootimg-native:do_populate_sysroot"
SSTATETASKS += "do_makeboot"
SSTATE_SKIP_CREATION_task-makeboot = '1'
do_makeboot[sstate-inputdirs] = "${BOOTIMGDEPLOYDIR}"
do_makeboot[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[stamp-extra-info] = "${MACHINE_ARCH}"

python do_makeboot_setscene () {
    sstate_setscene(d)
}

python () {
    if bb.utils.contains('MACHINE_FEATURES', 'qti-vm', False, True, d):
        bb.build.addtask('do_makeboot_setscene', None, None, d)
        bb.build.addtask('do_makeboot', 'do_image_complete', None, d)
}
