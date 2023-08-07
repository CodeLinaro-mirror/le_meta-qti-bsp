# QTI Linux mbb minimal image file.
# Provides packages required to build an mbb minimal image with
# boot to console

inherit qimage

DEPENDS += "mtd-utils-native"
USBCOMPOSITION_sdxpinn = "${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-core', '90DB', '4EE7', d)}"

IMAGE_FEATURES += "read-only-rootfs nand2x ssh-server-openssh persist-volume"

CORE_IMAGE_EXTRA_INSTALL += "\
              glib-2.0 \
              kernel-modules \
              coreutils \
              powerapp \
              powerapp-powerconfig \
              powerapp-reboot \
              powerapp-shutdown \
              systemd-machine-units \
              packagegroup-android-utils \
              packagegroup-startup-scripts \
              ${@bb.utils.contains('MACHINE_FEATURES', 'nand-boot', 'mtd-utils-ubifs', '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'location', \
                    bb.utils.contains('MACHINE_FEATURES', 'qti-location', 'packagegroup-qti-location', '', d), '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-ss-mgr', 'packagegroup-qti-ss-mgr', '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-core', 'packagegroup-qti-core', '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'msm-data', 'packagegroup-qti-data', '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-ss-mgr-prop', \
                    bb.utils.contains('MACHINE_SUPPORTS_PDMAPPER', 'True', 'ss-services', '', d), '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-ss-mgr-prop', \
                    bb.utils.contains('MACHINE_SUPPORTS_SSR', 'True', 'subsystem-ramdump', '', d), '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-sec', \
                    bb.utils.contains('MACHINE_FEATURES', 'qti-security', 'packagegroup-qti-securemsm', '', d), '', d)} \
"

#Install bash
CORE_IMAGE_EXTRA_INSTALL += "bash"
