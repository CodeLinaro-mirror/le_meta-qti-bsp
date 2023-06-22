# QTI Linux mbb minimal image file.
# Provides packages required to build an mbb minimal image with
# boot to console

inherit qimage

IMAGE_FEATURES += "read-only-rootfs ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
              glib-2.0 \
              kernel-modules \
              powerapp \
              powerapp-powerconfig \
              powerapp-reboot \
              powerapp-shutdown \
              systemd-machine-units \
              packagegroup-android-utils \
              packagegroup-startup-scripts \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'location', \
                    bb.utils.contains('MACHINE_FEATURES', 'qti-location', 'packagegroup-qti-location', '', d), '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-ss-mgr', 'packagegroup-qti-ss-mgr', '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-core', 'packagegroup-qti-core', '', d)} \
              ${@bb.utils.contains('BBFILE_COLLECTIONS', 'msm-data', 'packagegroup-qti-data', '', d)} \
"

#Install bash
CORE_IMAGE_EXTRA_INSTALL += "bash"
