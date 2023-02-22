SUMMARY = "QTI headless image"
DESCRIPTION = "QTI headless image for basic platform support without Multimedia/Connectivity modules"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "ext4-utils-native mkbootimg-native"

inherit core-image

IMAGE_ROOTFS_SIZE = "716800"

IMAGE_INSTALL += " \
    system-core-adbd \
    system-core-leprop \
    system-core-post-boot \
    system-core-usb \
    system-prop \
"

IMAGE_LINGUAS = ""

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

remove_data_bind() {
    sed -i '/^\/data/d' ${IMAGE_ROOTFS}/etc/fstab
}

ROOTFS_POSTPROCESS_COMMAND:append = " remove_data_bind;"
