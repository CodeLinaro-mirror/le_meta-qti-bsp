# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted (subject to the limitations in the
# disclaimer below) provided that the following conditions are met:
#
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#       with the distribution.
#
#    * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#      contributors may be used to endorse or promote products derived
#           from this software without specific prior written permission.
#
# NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE
# GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
# HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
# IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
# GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
# IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

# Generates boot.img with verity keys for vdlkm, system added to vendor-ramdisk

DEPENDS += "cryptsetup-native openssl-native"

CONFLICT_MACHINE_FEATURES += " dm-verity-bootloader dm-verity-initramfs"

BOOTIMGDEPLOYDIR = "${WORKDIR}/deploy-${PN}-bootimage-complete"

INITRAMFS_IMAGE ?= ''
RAMDISK = "${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.${INITRAMFS_FSTYPES}"
VRAMDISK = "${DEPLOY_DIR_IMAGE}/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.${INITRAMFS_FSTYPES}"

def get_ramdisk_path(d):
    if os.path.exists(d.getVar('RAMDISK')):
        return '%s' %(d.getVar('RAMDISK'))
    return '/dev/null'

RAMDISK_PATH = "${@get_ramdisk_path(d)}"

MKBOOTUTIL = '${@oe.utils.conditional("PREFERRED_PROVIDER_virtual/mkbootimg-native", "mkbootimg-gki-native", "scripts/mkbootimg.py", "mkbootimg", d)}'

# For initramfs based dm-verity solution on vendor_dlkm and system partitions,computed hash values
# need to be added into vendor_ramdisk before generating boot.img
pack_verity_metadata_into_vendor_ramdisk[cleandirs] += "${WORKDIR}/vramdisk ${WORKDIR}/vramdisk_cpio_append"
pack_verity_metadata_into_vendor_ramdisk() {
    echo "Copying verity metadata into vendor-ramdisk ..."
    compressType="cpio"
    for img in cpio cpio.gz cpio.lz4; do
        if [ -e "${DEPLOY_DIR_IMAGE}/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img" ]; then
            cp ${DEPLOY_DIR_IMAGE}/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img ${WORKDIR}/vramdisk/.
            compressType="cpio"
            case $img in
            cpio.gz)
                echo "gzip decompressing image"
                cp ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img.org
                gunzip -f ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img
                compressType="cpio.gz"
                break
                ;;
            cpio.lz4)
                echo "lz4 decompressing image"
                lz4 -df ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.cpio
                cp ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$img.org
                compressType="cpio.lz4"
                break
                ;;
            esac
            break
        fi
    done
    # Verify that the above step found a valid initramfs, fail otherwise
    [ -f ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.cpio ] && echo "Finished copy of initramfs into ${WORKDIR}/vramdisk" || die "No valid .cpio found"

    # Copy verity keys into vramdisk_cpio_append folder
    mkdir -p ${WORKDIR}/vramdisk_cpio_append/verity
    VERITY_SALT="aee087a5be3b982978c923f566a94613496b417f2af592639bc80d141e34dfe7"
    BLOCK_SIZE="4096"
    FEC_ROOTS="2"

    #Compute for vendor_dlkm
    root_hash=`awk -F ':' '{ if ($1 == "Root hash") print $2 }' ${WORKDIR}/vdlkm_verity_metadata.txt | sed "s/^[ \t]*//"`
    data_blocks=`awk -F ':' '{ if ($1 == "Data blocks") print $2 }' ${WORKDIR}/vdlkm_verity_metadata.txt  |  sed "s/^[ \t]*//"`
    fec_offset=`awk -F ':' '{ if ($1 == "fec_offset") print $2 }' ${WORKDIR}/vdlkm_verity_metadata.txt  |  sed "s/^[ \t]*//"`
    hash_offset=`expr $data_blocks \* 4096`

    cat > ${WORKDIR}/vramdisk_cpio_append/verity/vdlkm.env  <<EOF
VERITY_DATA_BLOCKS=${data_blocks}
VERITY_HASH_OFFSET=${hash_offset}
VERITY_FEC_OFFSET=${fec_offset}
VERITY_FEC_ROOTS=${FEC_ROOTS}
VERITY_SALT=${VERITY_SALT}
VERITY_ROOT_HASH=${root_hash}
EOF
    echo "completed computing root_hash for vdlkm"

    # Sign the root hash
    echo -n "${root_hash}" > ${WORKDIR}/roothash.txt
    openssl smime -sign -nocerts -noattr -binary -in ${WORKDIR}/roothash.txt \
            -inkey ${STAGING_DIR_TARGET}/kernel-certs/verity_key.pem -signer \
            ${STAGING_DIR_TARGET}/kernel-certs/verity_cert.pem -outform der -out ${WORKDIR}/vramdisk_cpio_append/verity/vdlkm.sig
    echo "completed signing root_hash for vdlkm"

    #Compute for system
    root_hash=`awk -F ':' '{ if ($1 == "Root hash") print $2 }' ${WORKDIR}/system_verity_metadata.txt | sed "s/^[ \t]*//"`
    data_blocks=`awk -F ':' '{ if ($1 == "Data blocks") print $2 }' ${WORKDIR}/system_verity_metadata.txt  |  sed "s/^[ \t]*//"`
    fec_offset=`awk -F ':' '{ if ($1 == "fec_offset") print $2 }' ${WORKDIR}/system_verity_metadata.txt  |  sed "s/^[ \t]*//"`
    hash_offset=`expr $data_blocks \* 4096`
    cat > ${WORKDIR}/vramdisk_cpio_append/verity/root.env  <<EOF
VERITY_DATA_BLOCKS=${data_blocks}
VERITY_HASH_OFFSET=${hash_offset}
VERITY_FEC_OFFSET=${fec_offset}
VERITY_FEC_ROOTS=${FEC_ROOTS}
VERITY_SALT=${VERITY_SALT}
VERITY_ROOT_HASH=${root_hash}
EOF
    echo "completed computing root_hash for system"

    # Sign the root hash
    echo -n "${root_hash}" > ${WORKDIR}/roothash.txt
    openssl smime -sign -nocerts -noattr -binary -in ${WORKDIR}/roothash.txt \
            -inkey ${STAGING_DIR_TARGET}/kernel-certs/verity_key.pem -signer \
            ${STAGING_DIR_TARGET}/kernel-certs/verity_cert.pem -outform der -out ${WORKDIR}/vramdisk_cpio_append/verity/root.sig
    echo "completed signing root_hash for system"

    # Pack all files from vramdisk_cpio_append folder into vendor-ramdisk cpio.
    (cd  ${WORKDIR}/vramdisk_cpio_append && find . -type f | cpio -ovA -H newc -F ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.cpio)

    # Compress again and Copy back to deploydir
    if [ -e "${DEPLOY_DIR_IMAGE}/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$compressType" ]; then
        rm ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$compressType
        lz4 -9 -z -l ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.cpio ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$compressType
        echo "Recreated $compressType image"
        cp -f ${WORKDIR}/vramdisk/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$compressType ${DEPLOY_DIR_IMAGE}/${VENDOR_INITRAMFS_IMAGE}-${MACHINE}.$compressType
        echo "Updated image in ${DEPLOY_DIR_IMAGE}"
    fi
}
do_makeboot[prefuncs] += "pack_verity_metadata_into_vendor_ramdisk"

# If BOOT_HEADER_VERSION >= 3, a vendor_boot image will be built
#  unless SKIP_VENDOR_BOOT is defined as True.
python do_makeboot () {
    import subprocess

    # Set cmdline
    cmdline=""
    if ((int(d.getVar("BOOT_HEADER_VERSION") or "0") < 3) or (d.getVar("SKIP_VENDOR_BOOT") or "True") == "True"):
        cmdline = " --cmdline " + "\"" + d.getVar('KERNEL_CMD_PARAMS', True) + "\""
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
    bb.debug(1, "dm-verity-none do_makeboot cmd: %s" % (cmd))
    try:
        ret = subprocess.check_output(cmd, shell=True)
    except RuntimeError as e:
        bb.error("dm-verity-none cmd: %s failed with error %s" % (cmd, str(e)))

}
do_makeboot[dirs]      = "${BOOTIMGDEPLOYDIR}/${IMAGE_BASENAME}"
# Make sure native tools and vmlinux ready to create boot.img
do_makeboot[depends] += "virtual/kernel:do_deploy virtual/mkbootimg-native:do_populate_sysroot"
do_makeboot[depends] += "${PN}:do_makevdlkm ${PN}:do_makesystem"

SSTATETASKS += "do_makeboot"
SSTATE_SKIP_CREATION_task-makeboot = '1'
do_makeboot[sstate-inputdirs] = "${BOOTIMGDEPLOYDIR}"
do_makeboot[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[stamp-extra-info] = "${MACHINE_ARCH}"

python do_makeboot_setscene () {
    sstate_setscene(d)
}
addtask do_makeboot_setscene

addtask do_makeboot after do_image before do_image_complete
