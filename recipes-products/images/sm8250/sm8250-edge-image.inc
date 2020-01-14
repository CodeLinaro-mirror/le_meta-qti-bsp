PACKAGE_CLASSES = "package_ipk"
#compile abl
RDEPENDS += "${@bb.utils.contains('TARGET_ARCH', 'arm', 'lib64-edk2', 'edk2', d)}"

EXTRA_USERS_PARAMS := ""
EXTRA_USERS_PARAMS_remove = "%"

ROOTFS_POSTPROCESS_COMMAND := ""
ROOTFS_POSTPROCESS_COMMAND_remove += "purge_rootfs;"
ROOTFS_POSTPROCESS_COMMAND_remove += "symlink_modules;"
ROOTFS_POSTPROCESS_COMMAND_remove += "sync_hostfs;"
ROOTFS_POSTPROCESS_COMMAND_remove += "set_user_group;"
ROOTFS_POSTPROCESS_COMMAND_remove += "zap_empty_root_password;"
ROOTFS_POSTPROCESS_COMMAND_remove += "rootfs_update_timestamp;"
ROOTFS_POSTPROCESS_COMMAND_remove += "read_only_rootfs_hook;"
ROOTFS_POSTPROCESS_COMMAND_remove += "set_systemd_default_target;"
ROOTFS_POSTPROCESS_COMMAND_remove += "systemd_create_users;"
ROOTFS_POSTPROCESS_COMMAND_remove += "do_fsconfig;"

PACKAGE_INSTALL_remove = "run-postinsts"
PACKAGE_INSTALL_remove += "packagegroup-base-extended"
PACKAGE_INSTALL_remove += "packagegroup-core-boot"

do_makesystem() {
     cp ${THISDIR}/${BASEMACHINE}/sm8250-fsconfig.conf ${WORKDIR}/rootfs-fsconfig.conf
#generate ext4 fs.
     make_ext4fs -C ${WORKDIR}/rootfs-fsconfig.conf -B ${DEPLOY_DIR_IMAGE}/system.map -s -a / ${IMAGE_EXT4_SELINUX_OPTIONS} -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMIMAGE_TARGET} ${IMAGE_ROOTFS}
     make_ext4fs -s -a /systemrw ${IMAGE_EXT4_SELINUX_OPTIONS} -b 4096 -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMRWIMAGE_TARGET}
}
