# QTI Linux minimal boot image file.
# Provides packages required to build an image with
# boot to console and wifi support.

inherit qimage

# use DISTRO_EXTRA_RDEPENDS = "list of packages"
# in distro conf file. These listed packages are specific to distro
# use MACHINE_EXTRA_RDEPENDS = "list of packages"
# these packages are complementary to image and specific to machine.
# specify IMAGE_FEATURES += "ssh-server-openssh" to bring in
#    packagegroup-core-ssh-openssh -> openssh

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += "\
              alsa-utils \
              glib-2.0 \
              kernel-modules \
              packagegroup-android-utils \
              packagegroup-filesystem-utils \
              packagegroup-qti-audio \
              ${@bb.utils.contains('COMBINED_FEATURES', 'qti-bluetooth', "packagegroup-qti-bluetooth", "", d)} \
              packagegroup-qti-camera \
              ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'packagegroup-qti-containers', '', d)} \
              packagegroup-qti-core \
              packagegroup-qti-data \
              packagegroup-qti-display \
              packagegroup-qti-dsp \
              packagegroup-qti-fastcv \
              packagegroup-qti-fastmmi \
              packagegroup-qti-gfx \
              packagegroup-qti-gst \
              packagegroup-qti-ml \
              packagegroup-qti-qmmf \
              packagegroup-qti-pulseaudio \
              packagegroup-qti-robotics \
              packagegroup-qti-ss-mgr \
              ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', "packagegroup-qti-securemsm", "", d)} \
              packagegroup-qti-sensors-see \
              packagegroup-qti-test-sensors-see \
              packagegroup-qti-video \
              ${@bb.utils.contains('COMBINED_FEATURES', 'qti-wifi', "packagegroup-qti-wifi", "", d)} \
              ${@bb.utils.contains('DISTRO_FEATURES', 'ros2', 'packagegroup-ros2-foxy', '', d)} \
              packagegroup-startup-scripts \
              packagegroup-support-utils \
              systemd-machine-units \
              ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
"

CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "alsa-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "kernel-modules"
CORE_IMAGE_EXTRA_INSTALL:append:qrbx210-rbx = " gki-kernel-modules-second-stage diag-router"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-filesystem-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-audio"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-bluetooth"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-camera"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-containers"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-core"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-data"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-dsp"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-fastcv"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-fastmmi"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-gst"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-ml"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-qmmf"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-pulseaudio"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-robotics"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-ss-mgr"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-securemsm"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-test-sensors-see"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-video"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-qti-wifi"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-ros2-foxy"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-support-utils"
CORE_IMAGE_EXTRA_INSTALL:remove:qrbx210-rbx = "packagegroup-selinux-minimal"
