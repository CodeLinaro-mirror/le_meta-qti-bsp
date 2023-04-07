SUMMARY = "Initramfs image for early-ramdisk-init"
LICENSE = "BSD-3-Clause-Clear"

IMAGE_CLASSES:remove = "qimage qimage-boot"

inherit image

DEPENDS += "mkbootimg-native virtual/kernel"

IMAGE_FSTYPES = "cpio.lz4"

# avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""
KERNELDEPMODDEPEND = ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

# We really need just kexecboot, kexec and ubiattach
IMAGE_INSTALL = "\
                 early-ramdisk-init libgcc kmod util-linux-libblkid \
                 kernel-module-soc-sleep-stats-${KERNEL_VERSION} \
                 kernel-module-boot-stats-${KERNEL_VERSION} \
                 kernel-module-spidev-${KERNEL_VERSION} \
                 kernel-module-spi-msm-geni-${KERNEL_VERSION} \
                 kernel-module-qcom-smd-${KERNEL_VERSION} \
                 kernel-module-rproc-qcom-common-${KERNEL_VERSION} \
                 kernel-module-qcom-ramdump-${KERNEL_VERSION} \
                 kernel-module-ns-${KERNEL_VERSION} \
                 kernel-module-qrtr-${KERNEL_VERSION} \
                 kernel-module-qmi-helpers-${KERNEL_VERSION} \
                 kernel-module-qcom-sysmon-${KERNEL_VERSION} \
                 kernel-module-qcom-q6v5-${KERNEL_VERSION} \
                 kernel-module-qcom-pil-info-${KERNEL_VERSION} \
                 kernel-module-mdt-loader-${KERNEL_VERSION} \
                 kernel-module-smp2p-${KERNEL_VERSION} \
                 kernel-module-qcom-q6v5-pas-${KERNEL_VERSION} \
                 audiodlkm \
"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

IMAGE_LINGUAS = ""

FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# disable runtime dependency on run-postinsts -> update-rc.d
ROOTFS_BOOTSTRAP_INSTALL = ""

