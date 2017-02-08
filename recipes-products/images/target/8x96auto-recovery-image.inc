DEPENDS += "bzip2 fsconfig-native applypatch-native bsdiff-native mkbootimg-native"

IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

# The OE-Core minimal boot image files.
IMAGE_INSTALL += "packagegroup-core-boot"
IMAGE_INSTALL += "${ROOTFS_PKGMANAGE_BOOTSTRAP}"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "recovery"
IMAGE_INSTALL += "start-scripts-find-recovery-partitions"
IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "mtd-utils-ubifs"
IMAGE_INSTALL += "e2fsprogs-e2fsck"
IMAGE_INSTALL += "e2fsprogs-mke2fs"
IMAGE_INSTALL += "libcomerr"
IMAGE_INSTALL += "libss"
IMAGE_INSTALL += "libe2p"
IMAGE_INSTALL += "libext2fs"

do_makerecovery() {
        echo "Deploy image : ${DEPLOY_DIR_IMAGE}"
	mkbootimg --kernel "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}" --ramdisk "${DEPLOY_DIR_IMAGE}/machine-recovery-image-8x96auto.cpio.gz"  -o "${DEPLOY_DIR_IMAGE}/recovery.img" --cmdline "${RECOVERY_KERNEL_CMDLINE}" --base "${KERNEL_BASE}"
}
