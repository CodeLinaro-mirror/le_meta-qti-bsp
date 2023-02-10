BASEDISTRO ?= "msm"
require conf/distro/include/msm.inc

require conf/distro/include/security_flags.inc

require conf/distro/include/distro_features.inc

DISTRO_NAME = "automotive"
DISTROOVERRIDES =. "automotive:"
# Support SDP
ID_LIKE = "automotive"

PRODUCT ?= "${MACHINE}" 
DEPLOY_NAME_BASE = "${PRODUCT}-automotive"
IMAGE_NAME = "${IMAGE_BASENAME}-${PRODUCT}${IMAGE_VERSION_SUFFIX}"
IMAGE_LINK_NAME = "${IMAGE_BASENAME}-${PRODUCT}"

PERSIST_IMAGE_ROOTFS_SIZE ?= "128M"

VIRTUAL-RUNTIME_dev_manager = "udev"
VIRTUAL-RUNTIME_init_manager = "systemd"

INCOMPATIBLE_LICENSE = "${@bb.utils.contains('LAYERSERIES_COMPAT_yocto', 'dunfell', 'GPL-3.0 LGPL-3.0 AGPL-3.0', 'GPL-3.0-only LGPL-3.0-only AGPL-3.0-only', d)}"

IMAGE_FEATURES:append = " read-only-rootfs"

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
