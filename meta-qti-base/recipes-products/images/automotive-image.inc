IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-core', 'packagegroup-qti-core-minimal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', 'packagegroup-qti-security', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-tools', 'packagegroup-qti-tools', '', d)} \
    "

IMAGE_LINGUAS = ""

EXTRA_IMAGECMD_ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "1572864"

SSTATE_MANFILEPREFIX = "${@bb.utils.contains('PERF_BUILD', '1', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}-perf', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}' , d)}"

SDK_DEPLOY = "${DEPLOY_DIR}/sdk-${PRODUCT}"

# Add kernel header to SDK.
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"

BAD_RECOMMENDATIONS += "rng-tools"
