# QTI Linux multimedia minimal image file.
# Provides packages required to build an image with
# all multimedia support enabled.

inherit qimage

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
        glib-2.0 \
        kernel-modules \
        packagegroup-android-utils \
        packagegroup-filesystem-utils \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        packagegroup-qti-core \
        packagegroup-qti-data \
        systemd-machine-units \
"
