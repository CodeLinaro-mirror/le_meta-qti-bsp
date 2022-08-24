BASEDISTRO ?= "msm"
require conf/distro/include/msm.inc

require conf/distro/include/security_flags.inc

DISTRO_NAME = "automotive"
DISTROOVERRIDES =. "automotive:"
# Support SDP
ID_LIKE = "automotive"

PRODUCT ?= "${MACHINE}" 
DEPLOY_NAME_BASE = "${PRODUCT}-automotive"
IMAGE_NAME = "${IMAGE_BASENAME}-${PRODUCT}${IMAGE_VERSION_SUFFIX}"
IMAGE_LINK_NAME = "${IMAGE_BASENAME}-${PRODUCT}"

# Support systemd-user-session
DISTRO_FEATURES:append = " pam"

# Video
DISTRO_FEATURES:append:sa6155 = " qti-carcamsrc qti-omx qti-fastcv"
DISTRO_FEATURES:append:sa81x5-rt = " qti-carcamsrc qti-omx qti-fastcv"
DISTRO_FEATURES:append:sa81x5 = " qti-carcamsrc qti-omx qti-fastcv"
DISTRO_FEATURES:append:sa8295 = " qti-codec2"
DISTRO_FEATURES:append:lemans = " qti-codec2"
DISTRO_FEATURES:append:qtiquingvm = " qti-omx qti-gstqeavb"
DISTRO_FEATURES:append:qtiquingvm8295 = " qti-codec2 qti-gstqeavb"
DISTRO_FEATURES:append:quin-gvm-gen4 = " qti-codec2"

# Display
DISTRO_FEATURES:append = " wayland"

# Use synergy, disable bluez
DISTRO_FEATURES_BACKFILL_CONSIDERED += "bluez5 bluetooth"

#Enable persist-image
DISTRO_FEATURES:append = " persist-image "
PERSIST_IMAGE_ROOTFS_SIZE ?= "128M"

DISTRO_FEATURES:append = " early-eth"
#DISTRO_FEATURES:append = " early_init"
#DISTRO_FEATURES:append = " early_userspace"


DISTRO_FEATURES:append = " hibernation"

# Use systemd init manager.
DISTRO_FEATURES:append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_dev_manager = "udev"
VIRTUAL-RUNTIME_init_manager = "systemd"

DISTRO_FEATURES:append = " ipa3"
DISTRO_FEATURES:append = " vulkan"

# WLAN
DISTRO_FEATURES:append = " qti-wlan wifi"

# Support kdump
#DISTRO_FEATURES:append = " kdump-support"

INCOMPATIBLE_LICENSE = "${@bb.utils.contains('LAYERSERIES_COMPAT_yocto', 'dunfell', 'GPL-3.0 LGPL-3.0 AGPL-3.0', 'GPL-3.0-only LGPL-3.0-only AGPL-3.0-only', d)}"

# Enable FDE
DISTRO_FEATURES:append:qtiquingvm = " qti-fde"
DISTRO_FEATURES:append:qtiquingvm8295 = " qti-fde"
DISTRO_FEATURES:append:quin-gvm-gen4 = " qti-fde"

IMAGE_FEATURES:append = " read-only-rootfs"

LICENSE_FLAGS_WHITELIST += "commercial_gstreamer1.0-libav commercial_gstreamer1.0-omx"
LICENSE_FLAGS_WHITELIST += "commercial_gstreamer1.0-plugins-ugly commercial_opencore-amr"
LICENSE_FLAGS_WHITELIST += "commercial_ffmpeg"
LICENSE_FLAGS_ACCEPTED += "commercial_gstreamer1.0-libav commercial_gstreamer1.0-omx"
LICENSE_FLAGS_ACCEPTED += "commercial_gstreamer1.0-plugins-ugly commercial_opencore-amr"
LICENSE_FLAGS_ACCEPTED += "commercial_ffmpeg"

include ${DISTRO_INC_FILES}

# Sizes for EXT4 (in bytes)
SYSTEM_SIZE_EXT4 ?= "100000000"
SYSTEMRW_SIZE_EXT4 ?= "8388608"
USERDATA_SIZE_EXT4 ?= "2000000000"
CACHE_SIZE_EXT4 ?= "80000000"
PERSIST_IMAGE_ROOTFS_SIZE ?= "128M"

BB_DANGLINGAPPENDS_WARNONLY = "yes"
