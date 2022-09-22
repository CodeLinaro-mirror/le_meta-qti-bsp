# Provides packages required to build
# QTI Linux eXtended Reality image for Neo.

require qti-xreality-image.bb

# Add aurora supported package groups
CORE_IMAGE_EXTRA_INSTALL += "\
        gki-kernel-modules-second-stage \
        packagegroup-qti-camera-kernel \
        packagegroup-qti-mmframeworks \
        packagegroup-qti-ppat \
"

CORE_IMAGE_EXTRA_INSTALL += "bash"

# Remove unsupported package groups
CORE_IMAGE_EXTRA_INSTALL_remove = "packagegroup-qti-cvp"
CORE_IMAGE_EXTRA_INSTALL_remove = "packagegroup-qti-gst"
CORE_IMAGE_EXTRA_INSTALL_remove = "packagegroup-qti-splitxr"
CORE_IMAGE_EXTRA_INSTALL_remove = "packagegroup-qti-splitxr-common"
CORE_IMAGE_EXTRA_INSTALL_remove = "packagegroup-qti-pulseaudio"

# Remove unsupported packages
CORE_IMAGE_EXTRA_INSTALL_remove = "gbm"
CORE_IMAGE_EXTRA_INSTALL_remove = "libdrm"
CORE_IMAGE_EXTRA_INSTALL_remove = "libdrm-tests"
CORE_IMAGE_EXTRA_INSTALL_remove = "libdrm-kms"

# Remove unsupported SDK packages
TOOLCHAIN_TARGET_TASK_remove = "ath6kl-utils-staticdev"

# Don't include kernel sources in SDK as prebuilt Kernel in use
TOOLCHAIN_TARGET_TASK_remove = "kernel-devsrc"
