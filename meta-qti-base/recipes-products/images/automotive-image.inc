IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-core', 'packagegroup-qti-core-minimal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-display', 'packagegroup-qti-display', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-multimedia', 'packagegroup-qti-multimedia', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', 'packagegroup-qti-security', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-data', 'packagegroup-qti-data', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-vnw', 'packagegroup-qti-vnw', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-data', 'packagegroup-qti-data', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-extra', 'packagegroup-qti-extra', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-multimedia', 'packagegroup-qti-multimedia', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-vnw', 'packagegroup-qti-vnw', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-wlan', 'packagegroup-qti-wlan', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-tools', 'packagegroup-qti-tools', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-audio', 'packagegroup-qti-audio', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-bluetooth', 'packagegroup-qti-bluetooth', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-location', 'packagegroup-qti-location-hal', '', d)} \
    "

IMAGE_LINGUAS = ""

EXTRA_IMAGECMD_ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "1572864"

SSTATE_MANFILEPREFIX = "${@bb.utils.contains('PERF_BUILD', '1', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}-perf', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}' , d)}"

SDK_DEPLOY = "${DEPLOY_DIR}/sdk-${PRODUCT}"

# Add kernel header to SDK.
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc linux-msm-headers-dev"

BAD_RECOMMENDATIONS += "rng-tools"
