# QTI Linux robotics image file.
# Provides packages required to build an image with
# robotics features support.

inherit qimage populate_sdk_qti

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
        alsa-utils \
        canutils \
        chronyc \
        e2fsprogs \
        e2fsprogs-e2fsck \
        e2fsprogs-mke2fs \
        e2fsprogs-tune2fs \
        glib-2.0 \
        gki-kernel-modules-second-stage \
        packagegroup-android-utils \
        packagegroup-qti-audio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-camera \
        packagegroup-qti-camera-kernel \
        ${@bb.utils.contains('DISTRO_FEATURES','virtualization', 'packagegroup-qti-containers', '', d)} \
        packagegroup-qti-core \
        packagegroup-qti-core-prop \
        packagegroup-qti-cvp \
        packagegroup-qti-data \
        packagegroup-qti-display \
        packagegroup-qti-dsp \
        packagegroup-qti-fastcv \
        packagegroup-qti-gfx \
        packagegroup-qti-gst \
        packagegroup-qti-ml \
        packagegroup-qti-mmframeworks \
        packagegroup-qti-qmmf \
        packagegroup-qti-robotics \
        packagegroup-qti-securemsm \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-test-sensors-see \
        packagegroup-qti-video \
        packagegroup-qti-wifi \
        ${@bb.utils.contains('DISTRO_FEATURES', 'ros2', 'packagegroup-ros2-foxy', '', d)} \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
        yavta \
"

CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "chronyc"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-bluetooth"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-cvp"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-fastcv"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-gst"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-mmframeworks"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-qmmf"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-robotics"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-securemsm"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-test-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-video"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-wifi"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-core-prop"
