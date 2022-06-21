# To add OTA upgrade support on an emmc/ufs target,
# add the MACHINE name to this list.
# This is the "only" list that will control whether
# OTA upgrade will be supported on a target.
DEPENDS += "releasetools-native zip-native fsconfig-native applypatch-native bc-native bsdiff-native"

RM_WORK_EXCLUDE_ITEMS += "rootfs rootfs-dbg"

IMAGE_SYSTEM_MOUNT_POINT = "/"

OTA_TARGET_IMAGE_ROOTFS_SQUASHFS = "${WORKDIR}/ota-target-image-squashfs"

OTA_TARGET_FILES_SQUASHFS = "target-files-squashfs.zip"
OTA_TARGET_FILES_SQUASHFS_PATH = "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${OTA_TARGET_FILES_SQUASHFS}"
OTA_FULL_UPDATE_SQUASHFS = "full_update_squashfs.zip"
OTA_FULL_UPDATE_SQUASHFS_PATH = "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${OTA_FULL_UPDATE_SQUASHFS}"
OTA_INCREMENTAL_UPDATE_SQUASHFS = "incremental_update_squashfs.zip"
OTA_INCREMENTAL_UPDATE_SQUASHFS_PATH = "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${OTA_INCREMENTAL_UPDATE_SQUASHFS}"

def get_filesmap(d):
    filesmap_path = ""
    overrides = (":" + (d.getVar("MACHINEOVERRIDES") or "")).split(":")
    overrides.reverse()

    for o in overrides:
        opath = "poky/meta-qti-bsp/recipes-bsp/base-files-recovery/" + o + "/radio/filesmap"
        path = os.path.join(d.getVar('WORKSPACEROOT'), opath)
        if os.path.exists(path):
            filesmap_path = path
            break
    return filesmap_path

#Create directory structure for targetfiles.zip
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/BOOTABLE_IMAGES"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/DATA"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/OTA"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/SYSTEM"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RADIO"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/IMAGES"
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/DTBO"

# Create this folder just for saving file_contexts(SElinux security context file),
# It will be used to generate OTA packages when selinux_fc is set.
do_recovery_squashfs[cleandirs] += "${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/BOOT/RAMDISK"

# recovery rootfs is required for generating OTA files.
# Wait till all tasks of machine-recovery-image complete.

do_recovery_squashfs() {
    echo "base image rootfs: ${IMAGE_ROOTFS}"

    # if exists copy filesmap into RADIO directory
    radiofilesmap=${@get_filesmap(d)}
    [[ ! -z "$radiofilesmap" ]] && install -m 755 $radiofilesmap ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RADIO/

    # copy the boot\recovery images
    cp ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${BOOTIMAGE_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/BOOTABLE_IMAGES/boot.img
    cp ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${BOOTIMAGE_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/BOOTABLE_IMAGES/recovery.img
    cp ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${BOOTIMAGE_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/IMAGES/boot.img
    cp ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${BOOTIMAGE_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/IMAGES/recovery.img

    #OTA scripts expect a sparse system image for block-based package
    squashfs2sparse ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${SYSTEMIMAGE_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/IMAGES/system.img

    cp ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${USERDATAIMAGE_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/IMAGES/userdata.img
    cp ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${USERDATAIMAGE_MAP_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/IMAGES/userdata.map

    # if dtbo.img file exist then copy
    if [ -f ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${DTBOIMAGE_TARGET} ]; then
        cp ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${DTBOIMAGE_TARGET} ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/IMAGES/dtbo.img
    fi

    # copy the contents of system rootfs
    cp -r ${IMAGE_ROOTFS}/. ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/SYSTEM/.
    cd  ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/SYSTEM
    rm -rf var/run
    ln -snf ../run var/run

    # copy the contents of system overlayfs
    cp -r ${IMAGE_ROOTFS}/overlay/. ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/DATA/.

    cp -r ${IMAGE_ROOTFS}/. ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY/.

    #generate recovery.fstab which is used by the updater-script
    echo #mount point fstype device [device2] >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY/recovery.fstab
    echo /boot     emmc  /dev/block/bootdevice/by-name/boot >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY/recovery.fstab
    echo /overlay     ext4  /dev/block/bootdevice/by-name/userdata >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY/recovery.fstab
    echo ${IMAGE_SYSTEM_MOUNT_POINT}   squashfs  /dev/block/bootdevice/by-name/system >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY/recovery.fstab

    #Getting content for OTA folder
    mkdir -p ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/OTA/bin
    cp ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY/usr/bin/applypatch ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/OTA/bin/.

    cp ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/RECOVERY/usr/bin/updater ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/OTA/bin/.

    # Pack releasetools.py into META folder itself.
    # This could also have been done by passing "--device_specific" to
    # ota_from_target_files.py but it would be hacky to find the absolute path there.
    cp ${STAGING_DIR_NATIVE}/usr/bin/releasetools/releasetools.py ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/.

    # copy contents of META folder
    #recovery_api_version is from recovery module
    echo recovery_api_version=3 >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    #blocksize = BOARD_FLASH_BLOCK_SIZE
    echo blocksize=131072 >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    export BOOT_SIZE=$(sed -r 's/.*label="boot_a".*size_in_kb="([0-9]+\.*[0-9]*).*/\1/;t;d' ${WORKDIR}/partition.xml)
    export SYSTEM_SIZE=$(sed -r 's/.*label="system_a".*size_in_kb="([0-9]+\.*[0-9]*).*/\1/;t;d' ${WORKDIR}/partition.xml)
    export USERDATA_SIZE=$(sed -r 's/.*label="userdata".*size_in_kb="([0-9]+\.*[0-9]*).*/\1/;t;d' ${WORKDIR}/partition.xml)

    # convert kb to bytes
    export BOOT_SIZE="$(expr $BOOT_SIZE \* 1024)"
    export SYSTEM_SIZE="$(expr $SYSTEM_SIZE \* 1024)"
    export USERDATA_SIZE="$(expr $USERDATA_SIZE \* 1024)"

    #boot_size: Size of boot partition from partition.xml
    echo "boot_size=0x$(echo "obase=16; $BOOT_SIZE" | bc)" >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    #system_size : Size of system partition from partition.xml
    echo "system_size=0x$(echo "obase=16; $SYSTEM_SIZE" | bc)" >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    #userdate_size : Size of data partition from partition.xml
    echo "userdate_size=0x$(echo "obase=16; $USERDATA_SIZE" | bc)" >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    #cache_size : Size of data partition from partition.xml
    echo "cache_size=0x$(echo "obase=16; $USERDATA_SIZE" | bc)" >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    #mkyaffs2_extra_flags : -c $(BOARD_KERNEL_PAGESIZE) -s $(BOARD_KERNEL_SPARESIZE)
    echo mkyaffs2_extra_flags=-c 4096 -s 16 >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    #extfs_sparse_flag : definition in build
    echo extfs_sparse_flags=-s >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    #default_system_dev_certificate : Dummy location
    echo default_system_dev_certificate=build/abcd >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    # set block img diff version to v3
    echo "blockimgdiff_versions=3" >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    # Targets that support A/B boot do not need recovery(fs)-updater
    echo le_target_supports_ab=1 >> ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS}/META/misc_info.txt

    cd ${OTA_TARGET_IMAGE_ROOTFS_SQUASHFS} && zip -qry ${OTA_TARGET_FILES_SQUASHFS_PATH} *
}
addtask do_recovery_squashfs after do_image_complete before do_build

# Generate OTA zip files
do_gen_ota_incremental_zip_squashfs[dirs] += "${DEPLOY_DIR_IMAGE}/ota-scripts"
do_gen_ota_incremental_zip_squashfs() {
    if [ -f "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${OTA_TARGET_FILES_SQUASHFS}" ]; then

        ./incremental_ota.sh ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${OTA_TARGET_FILES_SQUASHFS} ${OTA_TARGET_FILES_SQUASHFS_PATH} ${IMAGE_ROOTFS} ext4 --block --system_path ${IMAGE_SYSTEM_MOUNT_POINT}

        cp update_incr_ext4.zip ${DEPLOY_DIR_IMAGE}/${OTA_INCREMENTAL_UPDATE_SQUASHFS}
    else
        return 0
    fi
}

do_gen_ota_full_zip_squashfs[dirs] += "${DEPLOY_DIR_IMAGE}/ota-scripts"
do_gen_ota_full_zip_squashfs() {
    ./full_ota.sh ${OTA_TARGET_FILES_SQUASHFS_PATH} ${IMAGE_ROOTFS} ext4 --block --system_path ${IMAGE_SYSTEM_MOUNT_POINT}

    cp update_ext4.zip ${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}/${OTA_FULL_UPDATE_SQUASHFS}
}
addtask do_gen_ota_full_zip_squashfs after do_recovery_squashfs before do_build
