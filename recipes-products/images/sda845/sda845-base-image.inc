# Enable Bootloader
DEPENDS += "edk2"

# Packages to be included in rootfs creation.
IMAGE_INSTALL += "packagegroup-core-boot"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-core-selinux', '', d)}"
#IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','selinux', 'selinux-config', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','selinux', 'refpolicy-mls', '', d)}"

IMAGE_INSTALL += "start-scripts-firmware-links"
#IMAGE_INSTALL += "start-scripts-find-partitions"
IMAGE_INSTALL += "start-scripts-misc-daemon"
IMAGE_INSTALL += "start-script-ab-slot-util"

IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "base-passwd"
IMAGE_INSTALL += "openssl"
IMAGE_INSTALL += "glib-2.0"
IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "libxml2"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-logd"
IMAGE_INSTALL += "system-core-leprop"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "system-core-post-boot"
IMAGE_INSTALL += "system-conf"
IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "libusb1"
IMAGE_INSTALL += "usbutils"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "iperf"
IMAGE_INSTALL += "volatile-binds"
IMAGE_INSTALL += "abctl"
IMAGE_INSTALL += "libhardware"
IMAGE_INSTALL += "binder"
IMAGE_INSTALL += "kernel-modules"
IMAGE_INSTALL += "ffmpeg"
IMAGE_INSTALL += "av-frameworks"
IMAGE_INSTALL += "libstdc++"
IMAGE_INSTALL += "coreutils"
IMAGE_INSTALL += "media-headers"
IMAGE_INSTALL += "protobuf"
IMAGE_INSTALL += "sigma-dut"
IMAGE_INSTALL += "sec-config"
IMAGE_INSTALL += "libatomic"
IMAGE_INSTALL += "openssh"

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'full-disk-encryption', 'libscrypt', '', d)}"

# Install recovery(fs)-updater only for non-A/B variants
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', '', 'start-scripts-recovery-updater', d)}"

# Install recovery and its libs in system image if A/B boot is supported
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'recovery-bin', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'recovery', '', d)}"

#Install virtualization support packages
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'docker docker-registry python3-docker-compose', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'cgroup-lite aufs-util xz ca-certificates chrony', '', d)}"

# Below is to generate sparse ext4 system image (OE by default supports raw ext4 images)
do_makesystem() {
    make_ext4fs -B ${DEPLOY_DIR_IMAGE}/system.map -s ${IMAGE_EXT4_SELINUX_OPTIONS} -b 4096 -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMIMAGE_TARGET} ${IMAGE_ROOTFS}
    make_ext4fs -b 4096 -a /systemrw ${IMAGE_EXT4_SELINUX_OPTIONS} -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMRWIMAGE_TARGET}
}
