BASEMACHINE ?= "sdmsteppe"
require conf/machine/include/basemachine.inc

SOC_FAMILY ?= "qcs610"
include conf/machine/include/soc-family.inc

include conf/machine/include/arm/arch-armv8a.inc

PREFERRED_PROVIDER_virtual/kernel = "linux-msm"
PREFERRED_PROVIDER_virtual/mkbootimg = "mkbootimg-gki"
PREFERRED_PROVIDER_virtual/mkdtimg   = "mkdtimg-gki"
PREFERRED_PROVIDER_gtest = "googletest"
PREFERRED_VERSION_linux-msm ?= "6.6"
PREFERRED_VERSION_linux-msm-headers ?= "6.6"
PREFERRED_VERSION_kernel-toolchain ?= "6.6"
PREFERRED_VERSION_kernel-toolchain-native ?= "6.6"
PREFERRED_VERSION_dtc-android-build ?= "1.6.0"
PREFERRED_VERSION_mkbootimg-gki ?= "6.6"
PREFERRED_VERSION_mkbootimg-gki-native ?= "6.6"
PREFERRED_VERSION_mod-signing-keys ?= "6.6"
PREFERRED_VERSION_synx-kernel ?= "1.0"

PAGE_SIZE ?= '4096'

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
