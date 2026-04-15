# Provides packages required to build
# QTI Ramdisk archive with systemd as init

LICENSE = "BSD-3-Clause"

# Ramdisk image generation doesn't need abl
EXTRA_IMAGEDEPENDS:remove = "edk2"

EARLY_RAMDISK_KERNEL_MODULES ?= ""

PACKAGE_INSTALL = "\
    adbd \
    usb-composition \
    usb-composition-usbd \
    ${VIRTUAL-RUNTIME_base-utils} \
    ext4-utils \
    ${@d.getVar('kern_mods')} \
    fsmgr \
    early-ramdisk-init \
    gki-kernel-modules-linkmodulesload \
    gki-kernel-modules-first-stage \
    ${EARLY_RAMDISK_KERNEL_MODULES} \
    glib-2.0 \
    glibc \
    libbase \
    libcutils \
    libgcc \
    liblog \
    logwrapper \
    udev \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'libselinux libpcre', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', bb.utils.contains('MACHINE_FEATURES', 'dm-verity-initramfs-v3', 'cryptsetup verity-scripts lvm2-udevrules', '', d), '', d)} \
"


IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX = ""
IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""

# Set default target to initrd.target

inherit core-image

do_rootfs[nostamp] = "1"

# Add dependency on vendor ramdisk
python () {
    if ((int(d.getVar("BOOT_HEADER_VERSION") or "0") >= 3) and (d.getVar("SKIP_VENDOR_BOOT") or "True") == "False"):
        d.setVar("kern_mods", "")
        d.appendVarFlag('do_image', 'depends', ' ${VENDOR_INITRAMFS_IMAGE}:do_image_complete')
    else:
        d.setVar("kern_mods", "gki-kernel-modules-first-stage")
}

PACKAGE_INSTALL:remove:pineapple = "\
    adbd \
    usb-composition \
    usb-composition-usbd \
    first-stage-scripts-init \
"

create_init_symlink() {
    rm -rf ${IMAGE_ROOTFS}/init
    rm -rf ${IMAGE_ROOTFS}/sbin/init

    ln -sf ./usr/sbin/early-ramdisk-init ${IMAGE_ROOTFS}/init
}
ROOTFS_POSTPROCESS_COMMAND += "create_init_symlink; "
INCOMPATIBLE_LICENSE = "GPL-3.0* LGPL-3.0* AGPL-3.0*"
