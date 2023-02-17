# QTI Linux audio image file.
# Provides packages required to build an image with
# minimal boot to console with audio support.

inherit qimage

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
        glib-2.0 \
        kernel-modules \
        packagegroup-android-utils \
        packagegroup-filesystem-utils \
        packagegroup-android-utils-base \
        packagegroup-qti-audio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-core \
        packagegroup-qti-data \
        packagegroup-qti-dsp \
        packagegroup-qti-fastcv \
        packagegroup-qti-ml \
        packagegroup-qti-securemsm \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-wifi \
        packagegroup-startup-scripts \
        packagegroup-startup-scripts-base \
        packagegroup-support-utils \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
"

CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-audio"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-bluetooth"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-core"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-dsp"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-fastcv"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-securemsm"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-ss-mgr"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-qti-wifi"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-android-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-startup-scripts"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-support-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:vt-64 = "packagegroup-filesystem-utils"

# This image doesn't support abl generation
EXTRA_IMAGEDEPENDS:remove:vt-64 = "edk2"
