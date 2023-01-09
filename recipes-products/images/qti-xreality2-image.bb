# Provides packages required to build
# QTI Linux eXtended Reality image with splitxr support.

require qti-xreality2-base-image.bb

# Stop abl generation
EXTRA_IMAGEDEPENDS_remove = "edk2"

# Add aurora supported package groups
CORE_IMAGE_EXTRA_INSTALL += "\
        lxc \
        packagegroup-qti-splitxr \
        packagegroup-qti-splitxr-common \
"
