# QTI Linux robotics image file.
# Provides packages required to build an image with
# robotics features support.

inherit qimage populate_sdk_qti

IMAGE_FEATURES += "ssh-server-openssh"

ROS_FEATURES_PKG += "\
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'ros2-humble-layer', 'packagegroup-ros2-humble', '', d)} \
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'ros2-foxy-layer', 'packagegroup-ros2-foxy packagegroup-qti-ros2-node', '', d)} \
"

CORE_IMAGE_EXTRA_INSTALL += "\
        alsa-utils \
        can-utils \
        chronyc \
        glib-2.0 \
        packagegroup-android-utils \
        packagegroup-filesystem-utils \
        packagegroup-qti-audio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-core \
        packagegroup-qti-core-prop \
        diag-router \
        packagegroup-qti-eva \
        packagegroup-qti-display \
        packagegroup-qti-touch \
        packagegroup-qti-dsp \
        packagegroup-qti-gfx \
        packagegroup-qti-gst \
        packagegroup-qti-mmframeworks \
        packagegroup-qti-pulseaudio \
        packagegroup-qti-video \
        packagegroup-qti-wifi \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        systemd-machine-units \
        libdmabufheap \
        packagegroup-qti-perf \
"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "alsa-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "can-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "chronyc"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-camera"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-camera-kernel"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-cvp"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-eva"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-fastcv"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-mesa"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-gfx"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-mmframeworks"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-robotics"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-robos"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-robos-addon"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-securemsm"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-test-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-video"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "yavta"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-sensors-ship"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "packagegroup-qti-perf"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "tdk-chx01-get-data-app"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "tdk-thermistor-app"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "system-sample-apps"
CORE_IMAGE_EXTRA_INSTALL:remove:pineapple = "recovery-ab"

CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "chronyc"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-cvp"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-gst"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-robotics"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-test-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "tdk-chx01-get-data-app"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "tdk-thermistor-app"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "packagegroup-qti-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:append:kalama = " packagegroup-qti-qcawifi"
CORE_IMAGE_EXTRA_INSTALL:append:kalama = " tzdata tzcode"
CORE_IMAGE_EXTRA_INSTALL:append:kalama = " qcrosvm"
CORE_IMAGE_EXTRA_INSTALL:append:kalama = " vmsharememory-test"

# For lxc on kalama
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'packagegroup-qti-wifi', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'packagegroup-qti-qcawifi', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'packagegroup-qti-bluetooth', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'tzdata tzcode', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'qcrosvm', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'vmsharememory-test', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:append:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'lxc', '', d)}"
PERSISTIMAGE_TARGET:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'lxc-persist.img', 'persist.img', d)}"

CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-eva"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-ss-mgr"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-touch"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-perf"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-sensors-ship"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "packagegroup-qti-robos-addon"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "system-sample-apps"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "tdk-chx01-get-data-app"
CORE_IMAGE_EXTRA_INSTALL:remove:qrb5165 = "tdk-thermistor-app"
