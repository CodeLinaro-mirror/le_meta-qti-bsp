require automotive-image.inc
SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

DEPENDS += "mkbootimg-native"

inherit core-image

