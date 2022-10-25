require automotive-image.inc
SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

DEPENDS += "ext4-utils-native mkbootimg-native"
IMAGE_FEATURES += "sparse-image"

inherit core-image

