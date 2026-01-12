# This include forces the generation of a /usr userdata partition
# image file and propagates the /usr part of the filesystem
# over to it.

USR_IMAGE_BASENAME ?= "${PRODUCT}-usrfs"
USR_IMAGE_ROOTFS ?= "${TMPDIR}/rootfs/${USR_IMAGE_BASENAME}"
OUTPUT_FILE_EXT4 ?= "${DEPLOY_DIR_IMAGE}/${USERDATAIMAGE_TARGET}"

ROOTFS_POSTPROCESS_COMMAND:prepend = "clean_usr_rootfs; purge_rootfs; symlink_modules; sync_hostfs;"

# Generating userdata image in EXT4
IMAGE_PREPROCESS_COMMAND:append = "${@bb.utils.contains('IMAGE_FSTYPES','ext4',' create_usr_ext4_img;','', d)}"
do_image[depends] += "ext4-utils-native:do_populate_sysroot"

# Root image is created now
clean_usr_rootfs() {
   rm -rf ${USR_IMAGE_ROOTFS}
   mkdir -p ${USR_IMAGE_ROOTFS}
}

# Remove unwanted headers and sources copied into rootfs
purge_rootfs() {
    rm -rf ${IMAGE_ROOTFS}/usr/include
    rm -rf ${IMAGE_ROOTFS}/usr/src
    rm -rf ${IMAGE_ROOTFS}/src
}

symlink_modules() {

   DATA_DIR="${IMAGE_ROOTFS}/data"
   CONFIG_DIR="${DATA_DIR}/configs"
   LOGS_DIR="${DATA_DIR}/logs"

   if [ ! -d ${DATA_DIR} ]; then
       mkdir ${DATA_DIR}
   fi
   if [ ! -d ${CONFIG_DIR} ]; then
       mkdir ${CONFIG_DIR}
   fi
   if [ ! -d ${LOGS_DIR} ]; then
      mkdir ${LOGS_DIR}
   fi

   mv ${DATA_DIR}/* ${USR_IMAGE_ROOTFS}
}

sync_hostfs() {
   /bin/sync
}

create_userdata_file_contexts() {
    echo "/.* system_u:object_r:default_t:s0" > ${USR_IMAGE_ROOTFS}/file_contexts
}

create_usr_ext4_img() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        create_userdata_file_contexts
        make_ext4fs ${USRFS_FSCONFIG} ${MAKEEXT4_MOUNT_OPT} -s -S ${USR_IMAGE_ROOTFS}/file_contexts -a data -l ${USERDATA_SIZE_EXT4} ${OUTPUT_FILE_EXT4} ${USR_IMAGE_ROOTFS}
    else
        make_ext4fs ${USRFS_FSCONFIG} ${MAKEEXT4_MOUNT_OPT} -s -l ${USERDATA_SIZE_EXT4} ${OUTPUT_FILE_EXT4} ${USR_IMAGE_ROOTFS}
    fi
    chmod 644 ${OUTPUT_FILE_EXT4}
}

