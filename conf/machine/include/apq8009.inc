BASEMACHINE ?= "apq8009"
require conf/machine/include/basemachine.inc

SOC_FAMILY ?= "msm8909"
include conf/machine/include/soc-family.inc

# Uncomment the following line to enable the hard floating point abi. Note that
# this breaks some binary libraries and 3D (neither of which ship with
# meta-yocto). For maximum compatibility, leave this disabled.
#DEFAULTTUNE ?= "cortexa8hf-neon"

include conf/machine/include/tune-cortexa7.inc

BBMASK = ".*/*/recipes-kernel/linux-msm-4.4"

MACHINE_DISPLAY_WIDTH_PIXELS ?= "480"
MACHINE_DISPLAY_HEIGHT_PIXELS ?= "640"

SERIAL_CONSOLE ?= "115200 ttyHSL0"

DISTRO ?="msm"

DISTRO_FEATURES += "persist-image"

MACHINE_FEATURES += "kernel26 keyboard"

PAGE_SIZE ?= '2048'

#UBINIZE Args
UBINIZE_ARGS ?= "-m 2048 -p 128KiB"
#MKUBIFS Args
MKUBIFS_ARGS ?= "-m 2048 -e 126976 -c 4096 -F"
#PERSIST UBI Args
MKUBIFS_PERSIST_ARGS ?= "-m 2048 -e 126976 -c 4096 -j 2MiB -F -v"

#Volume sizes
SYSTEM_VOLUME_SIZE ?= "130MiB"
CACHE_VOLUME_SIZE ?= "70MiB"

# Max supported inodes
EXTRA_IMAGECMD ?= "-N 2048"

# Sizes for EXT4 (in bytes)
SYSTEM_SIZE_EXT4 ?= "1258292000"
USERDATA_SIZE_EXT4 ?= "2000000000"
CACHE_SIZE_EXT4 ?= "65536000"
