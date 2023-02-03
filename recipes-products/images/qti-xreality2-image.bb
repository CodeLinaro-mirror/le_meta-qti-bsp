# Provides packages required to build
# QTI Linux eXtended Reality image for Neo.

require qti-xreality-image.bb

# Stop abl generation
EXTRA_IMAGEDEPENDS_remove = "edk2"

# Add aurora supported package groups
CORE_IMAGE_EXTRA_INSTALL += "\
        lxc \
        packagegroup-qti-splitxr \
        packagegroup-qti-splitxr-common \
"

CORE_IMAGE_EXTRA_INSTALL += "bash"

# Remove unsupported package groups
CORE_IMAGE_EXTRA_INSTALL:remove = "packagegroup-qti-cvp"
CORE_IMAGE_EXTRA_INSTALL:remove = "packagegroup-qti-gst"
CORE_IMAGE_EXTRA_INSTALL:remove = "packagegroup-qti-splitxr"
CORE_IMAGE_EXTRA_INSTALL:remove = "packagegroup-qti-splitxr-common"
CORE_IMAGE_EXTRA_INSTALL:remove = "packagegroup-qti-pulseaudio"

# Remove unsupported packages
CORE_IMAGE_EXTRA_INSTALL:remove = "gbm"
CORE_IMAGE_EXTRA_INSTALL:remove = "libdrm"
CORE_IMAGE_EXTRA_INSTALL:remove = "libdrm-tests"
CORE_IMAGE_EXTRA_INSTALL:remove = "libdrm-kms"

# Remove unsupported SDK packages
TOOLCHAIN_TARGET_TASK:remove = "ath6kl-utils-staticdev"

# Don't include kernel sources in SDK as prebuilt Kernel in use
TOOLCHAIN_TARGET_TASK:remove = "kernel-devsrc"
