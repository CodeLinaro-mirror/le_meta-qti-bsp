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
        gki-kernel-modules-second-stage \
        packagegroup-android-utils \
        packagegroup-filesystem-utils \
        packagegroup-qti-audio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-camera \
        packagegroup-qti-camera-kernel \
        ${@bb.utils.contains('DISTRO_FEATURES','virtualization', 'packagegroup-qti-containers', '', d)} \
        packagegroup-qti-core \
        packagegroup-qti-core-prop \
        diag-router \
        packagegroup-qti-cvp \
        packagegroup-qti-eva \
        packagegroup-qti-data \
        packagegroup-qti-display \
        packagegroup-qti-touch \
        packagegroup-qti-dsp \
        packagegroup-qti-fastcv \
        packagegroup-mesa \
        packagegroup-qti-gfx \
        packagegroup-qti-gst \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-location', 'packagegroup-qti-location', '', d)} \
        packagegroup-qti-ml \
        packagegroup-qti-mmframeworks \
        packagegroup-qti-pulseaudio \
        packagegroup-qti-qmmf \
        packagegroup-qti-robotics \
        packagegroup-qti-robos \
        packagegroup-qti-robos-addon \
        packagegroup-qti-securemsm \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-test-sensors-see \
        packagegroup-qti-video \
        packagegroup-qti-wifi \
        ${@bb.utils.contains('DISTRO_FEATURES', 'ros2', '${ROS_FEATURES_PKG}', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qirp-sdk', 'packagegroup-qti-qirp', '', d)} \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-ib2c', 'qti-ib2c', '', d)} \
        ${@bb.utils.contains("COMBINED_FEATURES", "qti-uvc", "qti-umd-gadget", "", d)} \
        ${@bb.utils.contains("COMBINED_FEATURES", "qti-uvc", "qti-auto-framing-stabilization", "", d)} \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
        yavta \
        libdmabufheap \
        packagegroup-qti-sensors-ship \
        packagegroup-qti-sensors-see \
        packagegroup-qti-perf \
        tdk-chx01-get-data-app \
        tdk-thermistor-app \
        system-sample-apps \
        qti-c2-module \
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
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-wifi', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-qcawifi', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-bluetooth', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' tzdata tzcode', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' qcrosvm', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' vmsharememory-test', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:append:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' lxc', '', d)}"
PERSISTIMAGE_TARGET:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'lxc-persist.img', 'persist.img', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' gki-kernel-modules-second-stage', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-data', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-fastcv', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-mesa', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-qmmf', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-robos', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-robos-addon', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-securemsm', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-ss-mgr', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-sensors-ship', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' system-sample-apps', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' qti-c2-module', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-containers', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-location', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' ${ROS_FEATURES_PKG}', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-qirp', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' qti-ib2c', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' qti-umd-gadget', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' qti-auto-framing-stabilization', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-selinux-minimal', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' yavta', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-camera', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-camera-kernel', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' can-utils', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' chronyc', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-android-utils', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' diag-router', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-eva', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-dsp', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-pulseaudio', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-perf', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' packagegroup-qti-robos-addon-internal', '', d)}"
CORE_IMAGE_EXTRA_INSTALL:remove:kalama = "${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' recovery-ab', '', d)}"

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
