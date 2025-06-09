# QTI Linux multimedia image file.
# Provides packages required to build an image with
# all multimedia support enabled.

inherit qimage

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
        glib-2.0 \
        kernel-modules \
        libdrm-tests \
        graphite-client \
        alsa-utils \
        packagegroup-android-utils \
        packagegroup-filesystem-utils \
        packagegroup-qti-audio \
        packagegroup-qti-pulseaudio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-camera \
        packagegroup-qti-camera-kernel \
        packagegroup-qti-core \
        packagegroup-qti-data \
        packagegroup-qti-display \
        packagegroup-qti-dsp \
        packagegroup-qti-fastcv \
        packagegroup-qti-gst \
        packagegroup-qti-ml \
        packagegroup-qti-gfx \
        packagegroup-qti-qmmf \
        ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', 'packagegroup-qti-securemsm', '', d)} \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-sensors', 'packagegroup-qti-sensors', '', d)} \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-sensors-prop', 'packagegroup-qti-sensors-see', '', d)} \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-sensors-prop', 'packagegroup-qti-test-sensors-see', '', d)} \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-location', 'packagegroup-qti-location', '', d)} \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-video \
        packagegroup-qti-wifi \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
"

CORE_IMAGE_EXTRA_INSTALL:remove:qcm2290-mtp = "kernel-modules"
CORE_IMAGE_EXTRA_INSTALL:append:qcm2290-mtp = " gki-kernel-modules-second-stage"
CORE_IMAGE_EXTRA_INSTALL:append:qcm2290-mtp = " diag-router"
CORE_IMAGE_EXTRA_INSTALL:remove:qcs610-odk-64 = "kernel-modules"
CORE_IMAGE_EXTRA_INSTALL:append:qcs610-odk-64 = " gki-kernel-modules-second-stage"
CORE_IMAGE_EXTRA_INSTALL:append:qcs610-odk-64 = " diag-router"
CORE_IMAGE_EXTRA_INSTALL:remove:qcs610-odk-64 = "packagegroup-qti-display"
CORE_IMAGE_EXTRA_INSTALL:append:qcs610-odk-64 = " packagegroup-qcom-display"
CORE_IMAGE_EXTRA_INSTALL:remove:qcs610-odk-64 = "graphite-client"

CORE_IMAGE_EXTRA_INSTALL:remove:kera = "alsa-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "kernel-modules"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "libdrm-tests"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "graphite-client"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-audio"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-bluetooth"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-camera"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-camera-kernel"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-display"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-fastcv"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-touch"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-gfx"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-gst"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-pulseaudio"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-mmframeworks"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-qmmf"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-securemsm"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-ss-mgr"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-test-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-video"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-wifi"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-perf"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-sensors"
CORE_IMAGE_EXTRA_INSTALL:remove:kera = "packagegroup-qti-internal"
CORE_IMAGE_EXTRA_INSTALL:append:kera = " gki-kernel-modules-second-stage"
CORE_IMAGE_EXTRA_INSTALL:append:kera = " diag-router"
CORE_IMAGE_EXTRA_INSTALL:append:kera = " packagegroup-qti-core-prop"
