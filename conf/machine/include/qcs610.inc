BASEMACHINE ?= "sdmsteppe"
require conf/machine/include/basemachine.inc

SOC_FAMILY ?= "qcs610"
include conf/machine/include/soc-family.inc

include conf/machine/include/arm/arch-armv8a.inc

require recipes-kernel/linux-msm/msm-common.inc

MSM_KERNEL_VERSION = "6.6"

PREFERRED_PROVIDER_gtest = "googletest"
PREFERRED_VERSION_synx-kernel ?= "1.0"

# Max supported inodes
EXTRA_IMAGECMD ?= "-N 2048"

MACHINE_MNT_POINTS = "/firmware \
                      /overlay \
                      /dsp \
                      /bt_firmware \
                      /persist \
                     "

# Sizes for EXT4 (in bytes)
SYSTEM_SIZE_EXT4  ?= "1073741824"
USERDATA_SIZE ?= "8589934592"

# Formats of root filesystem images.
IMAGE_FSTYPES += "ext4 squashfs"

# On msm-5.4 or above ufs driver supports BSG Device.
UFS_BSG_DEV_USAGE = "True"

# On msm-5.4 abl supports DMMOD.
DM_MOD_FOR_KERNEL = "1"

# Disable some default features supported from upstream
MACHINE_FEATURES_BACKFILL_CONSIDERED += "qemu-usermode rtc"
