BASEMACHINE ?= "qrbx210"
require conf/machine/include/basemachine.inc

SOC_FAMILY ?= "bengal"
include conf/machine/include/soc-family.inc

DEFAULTTUNE ?= "aarch64"
require conf/machine/include/arm/arch-armv8a.inc

MACHINE_MNT_POINTS = "/firmware \
                      /dsp \
                      /bt_firmware \
                      /overlay \
                      /persist \
                     "

# Formats of root filesystem images.
IMAGE_FSTYPES += "ext4"

# Size for smallest contiguous block of physical memory
PAGE_SIZE ?= '4096'

# On msm-5.4 or above ufs driver supports BSG Device.
UFS_BSG_DEV_USAGE = "True"

#Disable some default features supported from upstream
MACHINE_FEATURES_BACKFILL_CONSIDERED += "qemu-usermode rtc"
