# Provides packages required to build
# Fully featured QTI Linux eXtended Reality image.

inherit qimage populate_sdk_qti

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
        glib-2.0 \
        gki-kernel-modules-second-stage \
        kernel-modules \
        packagegroup-android-utils \
        packagegroup-filesystem-utils \
        packagegroup-qti-audio \
        packagegroup-qti-bluetooth \
        packagegroup-qti-camera \
        packagegroup-qti-camera-kernel \
        packagegroup-qti-core \
        packagegroup-qti-display \
        packagegroup-qti-eva \
        packagegroup-qti-dsp \
        packagegroup-qti-fastcv \
        packagegroup-qti-gfx \
        packagegroup-qti-mmframeworks \
        packagegroup-qti-ppat \
        packagegroup-qti-securemsm \
        packagegroup-qti-sensors-see \
        packagegroup-qti-ss-mgr \
        packagegroup-qti-test-sensors-see \
        packagegroup-qti-video \
        packagegroup-qti-wifi \
        packagegroup-startup-scripts \
        packagegroup-support-utils \
        packagegroup-qti-perf \
        powerapp \
        powerapp-powerconfig \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
"

#Install packages for display
CORE_IMAGE_EXTRA_INSTALL += " \
            wayland \
            "

# Install bash shell
CORE_IMAGE_EXTRA_INSTALL += "bash"

# Remove unsupported SDK packages
TOOLCHAIN_TARGET_TASK:remove = "ath6kl-utils-staticdev"

# Don't include kernel sources in SDK as prebuilt Kernel in use
TOOLCHAIN_TARGET_TASK:remove = "kernel-devsrc"
