# Provides packages required to build
# QTI Linux eXtended Reality image.

inherit qimage populate_sdk_qti

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
        glib-2.0 \
        gki-kernel-modules-second-stage \
        kernel-modules \
        packagegroup-android-utils \
        packagegroup-qti-mmframeworks \
        packagegroup-filesystem-utils \
        packagegroup-qti-core \
        packagegroup-qti-dsp \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-securemsm \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        packagegroup-qti-video \
        packagegroup-qti-perf \
        powerapp \
        powerapp-powerconfig \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
"

# Install bash shell
CORE_IMAGE_EXTRA_INSTALL += "bash"

# Remove unsupported SDK packages
TOOLCHAIN_TARGET_TASK_remove = "ath6kl-utils-staticdev"
