# Rootfs creation.
IMAGE_INSTALL += "packagegroup-core-boot"
#IMAGE_INSTALL += "${ROOTFS_PKGMANAGE_BOOTSTRAP}"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

# Target SDK Packages
TOOLCHAIN_TARGET_TASK_append_sa415m += "glib-2.0-dev"
TOOLCHAIN_TARGET_TASK_append_sa415m += "common-headers"

# The MSM Linux minimal boot image files.
IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "base-passwd"
IMAGE_INSTALL += "glib-2.0"
IMAGE_INSTALL += "gdbserver"
IMAGE_INSTALL += "gdb"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "kernel-modules"
IMAGE_INSTALL += "libnl"
IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "libstdc++"
IMAGE_INSTALL += "libxml2"
IMAGE_INSTALL += "openssl"
IMAGE_INSTALL += "openssl-bin"
IMAGE_INSTALL += "perl"
IMAGE_INSTALL += "powerapp"
IMAGE_INSTALL += "powerapp-powerconfig"
IMAGE_INSTALL += "powerapp-reboot"
IMAGE_INSTALL += "powerapp-shutdown"
IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "volatile-binds"
IMAGE_INSTALL += "edk2"
IMAGE_INSTALL += "coreutils"
IMAGE_INSTALL += "km-loader"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-machine-units', 'start-scripts-find-partitions', d)}"
# include abl image which support rootfs type as ubifs
# this image should be flashed while using non-squashfs file
IMAGE_INSTALL += "abl-ubifs"

# Install A/B recovery bins and its libs in system image if A/B boot is supported
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'start-script-ab-slot-util recovery-ab-bin recovery-ab', '', d)}"
