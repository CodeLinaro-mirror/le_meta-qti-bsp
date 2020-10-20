IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-lxc', 'packagegroup-qti-lxc', '', d)} \
    "

# Add libgomp support
IMAGE_INSTALL += "libgomp libgomp-dev libgomp-staticdev"

# Add kernel header to SDK.
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"
