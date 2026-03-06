SUMMARY = "Initramfs image for early-ramdisk-init"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "mkbootimg-gki virtual/kernel"

IMAGE_CLASSES:remove = "qimage qimage-boot"

inherit image

IMAGE_FSTYPES = "cpio.lz4"

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"


# We really need just kexecboot, kexec and ubiattach

IMAGE_INSTALL = "\
    early-ramdisk-init libgcc kmod util-linux-libblkid \
    adbd \
    usb-composition \
    usb-composition-usbd \
    ${VIRTUAL-RUNTIME_base-utils} \
    ext4-utils \
    ${@d.getVar('kern_mods')} \
    fsmgr \
    glib-2.0 \
    glibc \
    initrd-release \
    libbase \
    libcutils \
    libgcc \
    liblog \
    logwrapper \
    packagegroup-core-boot \
    udev \
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

# Add dependency on vendor ramdisk
python () {
    if ((int(d.getVar("BOOT_HEADER_VERSION") or "0") >= 3) and (d.getVar("SKIP_VENDOR_BOOT") or "True") == "False"):
        d.setVar("kern_mods", "")
        d.appendVarFlag('do_image', 'depends', ' ${VENDOR_INITRAMFS_IMAGE}:do_image_complete')
    else:
        d.setVar("kern_mods", "gki-kernel-modules-first-stage")
}
IMAGE_NAME_SUFFIX = ""
