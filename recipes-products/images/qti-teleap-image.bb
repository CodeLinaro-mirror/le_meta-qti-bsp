# QTI Linux Telematics External AP image file.
# Provides packages required to build an image with
# all qti external AP (applications processor) support
# enabled.

inherit qimage populate_sdk

IMAGE_FEATURES += "read-only-rootfs"

CORE_IMAGE_EXTRA_INSTALL += "\
        glib-2.0 \
        i2c-tools \
        kernel-modules \
        net-tools \
        pps-tools \
        spitools \
        packagegroup-android-utils \
        ${@bb.utils.contains('MACHINE_FEATURES','emmc-boot', 'packagegroup-filesystem-utils', '', d)} \
        packagegroup-qti-core \
        packagegroup-qti-data \
        packagegroup-qti-teleap \
        packagegroup-qti-telematics \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-location', 'packagegroup-qti-location', '', d)} \
"
