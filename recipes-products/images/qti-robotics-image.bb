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
        kernel-modules \
        packagegroup-android-utils \
        packagegroup-qti-audio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-camera \
        ${@bb.utils.contains('DISTRO_FEATURES','virtualization', 'packagegroup-qti-containers', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES','ros2-foxy-sdk', 'packagegroup-ros2-foxy', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES','robotics-sdk', 'packagegroup-qti-robotics packagegroup-qti-robotics-prop', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES','qirp-sdk', 'packagegroup-qti-qirp packagegroup-qti-qirp-prop', '', d)} \
        packagegroup-qti-core \
        packagegroup-qti-core-prop \
        packagegroup-qti-cvp \
        packagegroup-qti-data \
        packagegroup-qti-display \
        packagegroup-qti-dsp \
        packagegroup-qti-fastcv \
        packagegroup-qti-gfx \
        packagegroup-qti-ml \
        packagegroup-qti-mmframeworks \
        packagegroup-qti-securemsm \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-test-sensors-see \
        packagegroup-qti-video \
        packagegroup-qti-qmmf \
        packagegroup-qti-wifi \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
        packagegroup-qti-sdk-depends-robotics \
        packagegroup-qti-sdk-depends-ros \
"

#Install packages for imud
CORE_IMAGE_EXTRA_INSTALL += " \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-sensors', 'imud', '', d)} \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-sensors', 'sensors-client', '', d)} \
"

# To include kernel headers in SDK
TOOLCHAIN_TARGET_TASK_append = " linux-msm-headers-dev"

# To include kernel sources in SDK for kernel modules
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"

# To include header files in SDK for sample code
TOOLCHAIN_TARGET_TASK_append = " camera-metadata-dev glm-dev opencv-staticdev"
TOOLCHAIN_HOST_TASK_append = " nativesdk-llvm-arm-toolchain"

# Remove docker-distribution-dev from SDK
PACKAGE_EXCLUDE = "docker-distribution-dev"
