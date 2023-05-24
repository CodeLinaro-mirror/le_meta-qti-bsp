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
"
IMAGE_INSTALL:sa81x5 += " \
                 kernel-module-spidev-${KERNEL_VERSION} \
                 kernel-module-spi-msm-geni-${KERNEL_VERSION} \
                 kernel-module-qcom-smd-${KERNEL_VERSION} \
                 kernel-module-rproc-qcom-common-${KERNEL_VERSION} \
                 kernel-module-qcom-ramdump-${KERNEL_VERSION} \
                 kernel-module-qmi-helpers-${KERNEL_VERSION} \
                 kernel-module-qcom-sysmon-${KERNEL_VERSION} \
                 kernel-module-qcom-q6v5-${KERNEL_VERSION} \
                 kernel-module-qcom-pil-info-${KERNEL_VERSION} \
                 kernel-module-mdt-loader-${KERNEL_VERSION} \
                 kernel-module-smp2p-${KERNEL_VERSION} \
                 kernel-module-qcom-q6v5-pas-${KERNEL_VERSION} \
                 kernel-module-pinctrl-spmi-gpio-${KERNEL_VERSION} \
                 kernel-module-gpucc-sm8150-${KERNEL_VERSION} \
                 kernel-module-sg-${KERNEL_VERSION} \
                 kernel-module-dcvs-fp-${KERNEL_VERSION} \
                 kernel-module-qcom-dcvs-${KERNEL_VERSION} \
                 kernel-module-mdt-loader-${KERNEL_VERSION} \
                 graphicsdlkm \
                 kernel-module-i2c-msm-geni-${KERNEL_VERSION} \
                 kernel-module-pinctrl-sx150x-${KERNEL_VERSION} \
                 kernel-module-i2c-mux-${KERNEL_VERSION} \
                 kernel-module-i2c-mux-pca954x-${KERNEL_VERSION} \
                 kernel-module-anx7625-${KERNEL_VERSION} \
                 securemsmdlkm \
                 displaydlkm \
                 kernel-module-videocc-sm8150-${KERNEL_VERSION} \
                 kernel-module-mdt-loader-${KERNEL_VERSION} \
                 videodlkm \
                 kernel-module-stub-regulator-${KERNEL_VERSION} \
                 cameradlkm \
"
python do_rootfs:prepend() {
    import re

    if(d.getVar('KERNEL_RAMDISK_DLKMS') == 'True'):
        kver = d.getVar('KERNEL_VERSION')

        f = open(d.getVar('STAGING_KERNEL_DIR') + "/modules.list.msm." + d.getVar('KERNEL_ARCH'))
        modules = f.readlines()
        f.close()

        for module in modules:
            tmp = module.replace("_", "-")
            kpack = tmp.split('.')[0]
            kpack = " kernel-module-" + kpack + "-" + kver
            d.appendVar('PACKAGE_INSTALL', kpack)
            bb.debug(1, "add install module: %s" % (kpack))
}

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

IMAGE_LINGUAS = ""

FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# disable runtime dependency on run-postinsts -> update-rc.d
ROOTFS_BOOTSTRAP_INSTALL = ""

