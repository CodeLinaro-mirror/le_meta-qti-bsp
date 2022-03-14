require automotive-image.inc
SUMMARY = "QTI headless image"
DESCRIPTION = "QTI headless image for basic platform support without Multimedia/Connectivity modules"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "ext4-utils-native mkbootimg-native"

inherit core-image

qti_headless_mount_partitions () {
    # Remove non-existing partitons in headless fstab
    sed -i -e '/vdd/d' ${IMAGE_ROOTFS}/etc/fstab
    sed -i -e '/vde/d' ${IMAGE_ROOTFS}/etc/fstab
    sed -i -e '/vdh/d' ${IMAGE_ROOTFS}/etc/fstab

    # Update the vbmeta.device path in headless target as it's different from the default value
    sed -i -e '/vbmeta.device/d'  ${IMAGE_ROOTFS}/build.prop
    sed -i -e '$avbmeta.device=/dev/vdd'  ${IMAGE_ROOTFS}/build.prop
}

ROOTFS_POSTPROCESS_COMMAND_append_qtiquingvm = " qti_headless_mount_partitions;"
