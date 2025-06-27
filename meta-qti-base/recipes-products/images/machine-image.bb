require automotive-image.inc
SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

DEPENDS += "ext4-utils-native mkbootimg-native"

inherit core-image

# Inherit selinux-image.bbclass to label selinux contexts during rootfs generation
inherit ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux-image', '', d)}
