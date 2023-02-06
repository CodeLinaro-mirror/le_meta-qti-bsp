# Provides packages required to build
# QTI Linux eXtended Reality image with splitxr support.

require qti-xreality2-base-image.bb

# Add aurora supported package groups
CORE_IMAGE_EXTRA_INSTALL += "\
        packagegroup-qti-splitxr \
        packagegroup-qti-splitxr-common \
"
