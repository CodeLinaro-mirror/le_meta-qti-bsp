# This include forces the generation of a /usr userdata partition
# image file and propagates the /usr part of the filesystem
# over to it.

DEPENDS += "ext4-utils-native"
USR_IMAGE_BASENAME       = "${MACHINE}-usrfs"
USR_IMAGE_ROOTFS         = "${TMPDIR}/rootfs/${USR_IMAGE_BASENAME}"
USR_IMAGE_ROOTFS_SIZE    = "65536"
USR_ROOTFS_SIZE          = "`du -ks ${USR_IMAGE_ROOTFS}|awk '{base_size = ($1 * ${IMAGE_OVERHEAD_FACTOR}); OFMT = "\"%.0f\"" ; print ((base_size > ${USR_IMAGE_ROOTFS_SIZE} ? base_size : ${USR_IMAGE_ROOTFS_SIZE}) + ${IMAGE_ROOTFS_EXTRA_SPACE}) }'`"
OUTPUT_FILE              = "${DEPLOY_DIR_IMAGE}/${USR_IMAGE_BASENAME}"
OUTPUT_FILE_YAFFS        = "${OUTPUT_FILE}.yaffs2"
OUTPUT_FILE_EXT4         = "${OUTPUT_FILE}.ext4"
OUTPUT_FILE_USR_UBIFS    = "${OUTPUT_FILE}.ubifs"
OUTPUT_FILE_FINAL_UBI    = "${DEPLOY_DIR_IMAGE}/${MACHINE}-sysfs.ubi"
OUTPUT_FILE_SYSTEM_UBIFS = "${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ubifs"
UBINIZE_CFG              = "${DEPLOY_DIR_IMAGE}/ubinize_system_userdata.cfg"

IMAGE_PREPROCESS_COMMAND_prepend    = "${@bb.utils.contains("DISTRO_FEATURES", "data-image", "clean_usr_rootfs; prelink_image; symlink_modules;", "", d)}"

# Generating userdata image in EXT4
IMAGE_PREPROCESS_COMMAND    += "${@bb.utils.contains("DISTRO_FEATURES", "data-image", "create_usr_ext4_img;", "", d)}"

# Generating single UBI device to be mounted on system partition
# It Contains three UBI volumes for system, userdata and cache
IMAGE_PREPROCESS_COMMAND += "${@base_contains('DISTRO_FEATURES', 'nand-boot', 'create_ubi_img;', '', d)}"

# Root image is created now
clean_usr_rootfs() {
   rm -rf ${USR_IMAGE_ROOTFS}
   mkdir -p ${USR_IMAGE_ROOTFS}
}

rename_img() {
   cp ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 ${DEPLOY_DIR_IMAGE}/${MACHINE}-sysfs.ext4
}

symlink_modules() {
   LIB_MODULES="${IMAGE_ROOTFS}/lib/modules"
   USR_LIB_MODULES="${IMAGE_ROOTFS}/usr/lib/modules"

   DATA_DIR="${IMAGE_ROOTFS}/data"
   HOME_DIR="${IMAGE_ROOTFS}/home"
   OPKG_DIR="${IMAGE_ROOTFS}/var/lib/opkg"
   CONFIG_DIR="${DATA_DIR}/configs"
   LOGS_DIR="${DATA_DIR}/logs"

   if [ ! -d ${DATA_DIR} ]; then
       mkdir ${DATA_DIR}
   fi
   if [ ! -d ${HOME_DIR} ]; then
       mkdir ${HOME_DIR}
   fi
   if [ ! -d ${CONFIG_DIR} ]; then
       mkdir ${CONFIG_DIR}
   fi
   if [ ! -d ${LOGS_DIR} ]; then
      mkdir ${LOGS_DIR}
   fi
   if [ ! -d ${OPKG_DIR} ]; then
      mkdir ${OPKG_DIR}
   fi

   mv ${DATA_DIR}/* ${USR_IMAGE_ROOTFS}
   cp -rf ${HOME_DIR}/* ${USR_IMAGE_ROOTFS}
   mkdir -p ${USR_IMAGE_ROOTFS}/var/lib/opkg
   cp -rf ${OPKG_DIR}/* ${USR_IMAGE_ROOTFS}/var/lib/opkg

   mkdir -p ${LIB_MODULES}
   mv ${LIB_MODULES} ${USR_LIB_MODULES}
   ln -sf /usr/lib/modules ${IMAGE_ROOTFS}/lib
}

create_usr_yaffs2_img() {
   mkyaffs2image ${USR_IMAGE_ROOTFS} ${OUTPUT_FILE_YAFFS}
   chmod 644 ${OUTPUT_FILE_YAFFS}
}

create_ubi_img() {
   create_ubinize_config
   mkfs.ubifs -r ${IMAGE_ROOTFS} -o ${OUTPUT_FILE_SYSTEM_UBIFS} ${MKUBIFS_ARGS}
   mkfs.ubifs -r ${USR_IMAGE_ROOTFS} -o ${OUTPUT_FILE_USR_UBIFS} ${MKUBIFS_ARGS}
   ubinize -o ${OUTPUT_FILE_FINAL_UBI} ${UBINIZE_ARGS} ${UBINIZE_CFG}
   chmod 644 ${OUTPUT_FILE_FINAL_UBI}
}

create_usr_ext4_img() {
   if [ "${BASEMACHINE}" != "mdm9607" ]; then
   make_ext4fs -s -l ${USERDATA_SIZE_EXT4} ${OUTPUT_FILE_EXT4} ${USR_IMAGE_ROOTFS}
   chmod 644 ${OUTPUT_FILE_EXT4}
   fi
}

create_ubinize_config() {
   echo \[sysfs_volume\] > "${UBINIZE_CFG}"
   echo mode=ubi >> "${UBINIZE_CFG}"
   echo image="${OUTPUT_FILE_SYSTEM_UBIFS}" >> "${UBINIZE_CFG}"
   echo vol_id=0 >> "${UBINIZE_CFG}"
   echo vol_type=dynamic >> "${UBINIZE_CFG}"
   echo vol_name=rootfs >> "${UBINIZE_CFG}"
   echo vol_size="${SYSTEM_VOLUME_SIZE}" >> "${UBINIZE_CFG}"

   echo \[usrfs_volume\] >> "${UBINIZE_CFG}"
   echo mode=ubi >> "${UBINIZE_CFG}"
   echo image="${OUTPUT_FILE_USR_UBIFS}" >> "${UBINIZE_CFG}"
   echo vol_id=1 >> "${UBINIZE_CFG}"
   echo vol_type=dynamic >> "${UBINIZE_CFG}"
   echo vol_name=usrfs >> "${UBINIZE_CFG}"
   echo vol_flags = autoresize >> "${UBINIZE_CFG}"

   echo \[cache_volume\] >> "${UBINIZE_CFG}"
   echo mode=ubi >> "${UBINIZE_CFG}"
   echo vol_id=2 >> "${UBINIZE_CFG}"
   echo vol_type=dynamic >> "${UBINIZE_CFG}"
   echo vol_name=cachefs >> "${UBINIZE_CFG}"
   echo vol_size="${CACHE_VOLUME_SIZE}" >> "${UBINIZE_CFG}"
}

