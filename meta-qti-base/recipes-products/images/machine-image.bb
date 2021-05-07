inherit core-image

include automotive-image.inc

IMAGE_LINGUAS = ""

DEPENDS += " mkbootimg-native ext4-utils-native "

EXTRA_IMAGECMD_ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "1572864"

SSTATE_MANFILEPREFIX="${@bb.utils.contains('PERF_BUILD', '1', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}-perf', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}' , d)}"

SDK_DEPLOY = "${DEPLOY_DIR}/sdk-${PRODUCT}"

BAD_RECOMMENDATIONS += " rng-tools"

# Disable multimedia packagegroup in Yocto master line before they are ready
IMAGE_INSTALL_remove += "${@bb.utils.contains('GLIBCVERSION', '2.33', 'packagegroup-qti-multimedia', '', d)}"
IMAGE_INSTALL_remove += "${@bb.utils.contains('GLIBCVERSION', '2.33', 'packagegroup-qti-multimedia-prop', '', d)}"
