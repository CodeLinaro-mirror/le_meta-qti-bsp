SUMMARY = "Initramfs image for early-ramdisk-init"
LICENSE = "BSD-3-Clause-Clear"

IMAGE_CLASSES:remove = "qimage qimage-boot"

inherit image

DEPENDS += "mkbootimg-native virtual/kernel"

IMAGE_FSTYPES = "cpio"

# avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""
KERNELDEPMODDEPEND = ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

# We really need just kexecboot, kexec and ubiattach
IMAGE_INSTALL = "\
                 early-ramdisk-init libgcc kmod \
                 kernel-module-soc-sleep-stats-${KERNEL_VERSION} \
                 kernel-module-boot-stats-${KERNEL_VERSION} \
"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

IMAGE_LINGUAS = ""

FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# disable runtime dependency on run-postinsts -> update-rc.d
ROOTFS_BOOTSTRAP_INSTALL = ""

