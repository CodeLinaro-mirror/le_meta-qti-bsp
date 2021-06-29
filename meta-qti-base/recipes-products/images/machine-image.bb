require automotive-image.inc
SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

DEPENDS += "ext4-utils-native mkbootimg-native"

inherit core-image

# Disable multimedia packagegroup in Yocto master line before they are ready
IMAGE_INSTALL_remove = "\
    ${@bb.utils.contains('GLIBCVERSION', '2.33', 'packagegroup-qti-multimedia', '', d)} \
    ${@bb.utils.contains('GLIBCVERSION', '2.33', 'packagegroup-qti-multimedia-prop', '', d)} \
"
