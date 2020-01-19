EXTRA_IMAGE_FEATURES += " package-management"

#compile abl
RDEPENDS += "${@bb.utils.contains('TARGET_ARCH', 'arm', 'lib64-edk2', 'edk2', d)}"

# Rootfs creation.
IMAGE_INSTALL += "packagegroup-core-boot"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

# The MSM Linux minimal boot image files.
IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "base-passwd"
IMAGE_INSTALL += "glib-2.0"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "kernel-modules"
IMAGE_INSTALL += "libnl"
IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "libstdc++"
IMAGE_INSTALL += "libxml2"
IMAGE_INSTALL += "openssl"

IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "system-core-logd"
IMAGE_INSTALL += "coreutils"
IMAGE_INSTALL += "system-core-leprop"
IMAGE_INSTALL += "system-core-post-boot"
IMAGE_INSTALL += "systemd-machine-units"
IMAGE_INSTALL += "volatile-binds"
IMAGE_INSTALL += "binder"

IMAGE_INSTALL += "ffmpeg"

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'start-script-ab-slot-util', '', d)}"

ROOTFS_POSTPROCESS_COMMAND_remove += "do_fsconfig;"

do_makesystem() {
     cp ${THISDIR}/${BASEMACHINE}/sm8250-fsconfig.conf ${WORKDIR}/rootfs-fsconfig.conf
     make_ext4fs -C ${WORKDIR}/rootfs-fsconfig.conf -B ${DEPLOY_DIR_IMAGE}/system.map -s -a / ${IMAGE_EXT4_SELINUX_OPTIONS} -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMIMAGE_TARGET} ${IMAGE_ROOTFS}
     make_ext4fs -s -a /systemrw ${IMAGE_EXT4_SELINUX_OPTIONS} -b 4096 -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMRWIMAGE_TARGET}
}
