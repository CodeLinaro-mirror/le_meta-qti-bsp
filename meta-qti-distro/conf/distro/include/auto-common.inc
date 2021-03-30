BASEDISTRO ?= "msm"
include conf/distro/msm.conf

DISTRO_NAME = "automotive"
DISTROOVERRIDES =. "automotive:"
# Support SDP
ID_LIKE = "automotive"

PRODUCT ?= "${MACHINE}" 
DEPLOY_NAME_BASE = "${PRODUCT}-automotive"
IMAGE_NAME = "${IMAGE_BASENAME}-${PRODUCT}${IMAGE_VERSION_SUFFIX}"
IMAGE_LINK_NAME = "${IMAGE_BASENAME}-${PRODUCT}"

# Support systemd-user-session
DISTRO_FEATURES_append = " pam"

# Display
DISTRO_FEATURES_append = " wayland"

# Use synergy, disable bluez
DISTRO_FEATURES_BACKFILL_CONSIDERED += "bluez5 bluetooth"

#Enable persist-image
DISTRO_FEATURES_append = " persist-image "
PERSIST_IMAGE_ROOTFS_SIZE ?= "128M"

DISTRO_FEATURES_append = " early-eth"
#DISTRO_FEATURES_append = " early_init"
#DISTRO_FEATURES_append = " early_userspace"

#DISTRO_FEATURES_append = " ab-boot-support"

DISTRO_FEATURES_append = " hibernation"

# Use systemd init manager.
DISTRO_FEATURES_append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_dev_manager = "udev"
VIRTUAL-RUNTIME_init_manager = "systemd"

DISTRO_FEATURES_append = " ipa3"
DISTRO_FEATURES_append = " vulkan"

# WLAN
DISTRO_FEATURES_append = " wifi"

# Support kdump
#DISTRO_FEATURES_append = " kdump-support"

INCOMPATIBLE_LICENSE = "GPL-3.0 LGPL-3.0 AGPL-3.0"

IMAGE_FEATURES_append = " read-only-rootfs"

LICENSE_FLAGS_WHITELIST += "commercial_gstreamer1.0-libav commercial_gstreamer1.0-omx"
LICENSE_FLAGS_WHITELIST += "commercial_gstreamer1.0-plugins-ugly commercial_opencore-amr"
LICENSE_FLAGS_WHITELIST += "commercial_ffmpeg"

include ${DISTRO_INC_FILES}

# Sizes for EXT4 (in bytes)
SYSTEM_SIZE_EXT4 ?= "100000000"
SYSTEMRW_SIZE_EXT4 ?= "8388608"
USERDATA_SIZE_EXT4 ?= "2000000000"
CACHE_SIZE_EXT4 ?= "80000000"
PERSIST_IMAGE_ROOTFS_SIZE ?= "128M"

BB_DANGLINGAPPENDS_WARNONLY = "yes"
