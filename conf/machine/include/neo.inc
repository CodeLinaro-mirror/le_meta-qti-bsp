BASEMACHINE ?= "neo"
require conf/machine/include/basemachine.inc

include conf/machine/include/waipio.inc

PAGE_SIZE ?= '4096'

MACHINE_MNT_POINTS = "/firmware \
                      /dsp \
                      /bt_firmware \
                      /overlay \
                      /persist \
                     "

# systemd auto generated mount paths
GENERATED_MACHINE_MNT_POINTS = "/lib/modules"

# Define userdata dir to be used
USERDATA_DIR = "${@bb.utils.contains('MACHINE_MNT_POINTS', '/overlay', 'overlay', 'data', d)}"

# Sizes for EXT4 partitions
SYSTEM_SIZE_EXT4 ?= "800MiB"
USERDATA_SIZE_EXT4 ?= "16GiB"
VDLKM_SIZE_EXT4 ?= "64MiB"

# Formats of root filesystem images.
IMAGE_FSTYPES += "ext4"

# Disable some default features supported from upstream
MACHINE_FEATURES_BACKFILL_CONSIDERED += "qemu-usermode rtc"
