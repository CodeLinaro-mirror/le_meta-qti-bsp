SUMMARY = "QTI Boot image"
DESCRIPTION = "Build QTI boot image with qimage-boot.bbclass"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "openssl-native python3-native virtual/kernel"

IMAGE_CLASSES:remove = "qimage"

inherit image qimage-boot

EXTRA_IMAGE_FEATURES = ""

do_rootfs[noexec] = "1"
do_image[noexec] = "1"
do_image_complete[noexec] = "1"

deltask ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '6.12', 'do_merge_dtbs', '', d)}
