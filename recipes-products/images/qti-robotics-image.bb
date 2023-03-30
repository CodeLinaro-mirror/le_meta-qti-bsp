# QTI Linux robotics image file.
# Provides packages required to build an image with
# robotics features support.

inherit qimage populate_sdk_qti

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
        alsa-utils \
        canutils \
        chronyc \
        glib-2.0 \
        gki-kernel-modules-second-stage \
        packagegroup-android-utils \
        packagegroup-filesystem-utils \
        packagegroup-qti-audio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-camera-kernel \
        ${@bb.utils.contains('DISTRO_FEATURES','virtualization', 'packagegroup-qti-containers', '', d)} \
        packagegroup-qti-core \
        packagegroup-qti-core-prop \
        diag-router \
        packagegroup-qti-cvp \
        packagegroup-qti-data \
        packagegroup-qti-display \
        packagegroup-qti-touch \
        packagegroup-qti-dsp \
        packagegroup-qti-fastcv \
        packagegroup-qti-gfx \
        packagegroup-qti-gst \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-location', 'packagegroup-qti-location', '', d)} \
        packagegroup-qti-ml \
        packagegroup-qti-mmframeworks \
        packagegroup-qti-qmmf \
        packagegroup-qti-robotics \
        packagegroup-qti-securemsm \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-test-sensors-see \
        packagegroup-qti-video \
        packagegroup-qti-wifi \
        ${@bb.utils.contains('BBFILE_COLLECTIONS', 'ros2-humble-layer', 'packagegroup-ros2-humble', '', d)} \
        ${@bb.utils.contains('BBFILE_COLLECTIONS', 'ros2-foxy-layer', 'packagegroup-ros2-foxy', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qirp-sdk', 'packagegroup-qti-qirp', '', d)} \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
        yavta \
        libdmabufheap \
        packagegroup-qti-sensors-ship \
        packagegroup-qti-perf \
"

CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "chronyc"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-camera"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-cvp"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-fastcv"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-mmframeworks"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-robotics"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-securemsm"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-test-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-perf"


CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-audio"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-camera"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-cvp"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-display"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-fastcv"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-gfx"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-gst"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-mmframeworks"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-qmmf"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-robotics"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-securemsm"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-ss-mgr"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-test-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-core-prop"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-core"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-camera-kernel"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-sensors-ship"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-touch"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-perf"

CORE_IMAGE_EXTRA_INSTALL:append:qrb5165 = " packagegroup-qti-gst-basic "
