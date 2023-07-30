SUMMARY = "QTI Boot image"
DESCRIPTION = "Build QTI boot image with qimage-boot.bbclass"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "openssl-native python3-native virtual/kernel"

IMAGE_CLASSES:remove = "qimage"

inherit image qimage-boot

EXTRA_IMAGE_FEATURES = ""

do_rootfs[noexec] = "1"
do_image[noexec] = "1"
do_image_complete[noexec] = "1"
