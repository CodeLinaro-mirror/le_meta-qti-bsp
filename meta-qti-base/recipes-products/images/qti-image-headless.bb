require automotive-image.inc
SUMMARY = "QTI headless image"
DESCRIPTION = "QTI headless image for basic platform support without Multimedia/Connectivity modules"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "ext4-utils-native mkbootimg-native"

inherit core-image

