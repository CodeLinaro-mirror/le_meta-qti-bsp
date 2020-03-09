# Additional non-open source packages to be put to the image filesystem.

DEPENDS += "ext4-utils-native"
#require target/common-auto-qti-image.inc

#include ${MACHINE}/${MACHINE}-image.inc

# Additional non-open source packages to be put to the image
# filesystem...
#
# Sizes for EXT4 (in bytes)
# if not defined, use the default value
USERDATA_SIZE_EXT4 ?= "2000000000"
CACHE_SIZE_EXT4 ?= "256M"
PERSIST_SIZE_EXT4 ?= "32M"
# system size should be IMAGE_ROOTFS_SIZE * 1024
SYSTEM_SIZE_EXT4 ?= "2147483648"

# Set up for handling the generation of the /usr image
# partition...
require mdm-usr-image.inc

# Set up for handling the generation of the /cache image
# partition...
require mdm-cache-image.inc

# Set up for handling the generation of the /persist image
# partition only for APQ Targets
require apq-persist-image.inc

#do_rootfs[nostamp] = "1"
#do_build[nostamp]  = "1"

# Below is to generate sparse ext4 system image (OE by default supports raw ext4 images)
#
create_sparsesystem() {

    ext2simg ${DEPLOY_DIR_IMAGE}/automotive-image-${MACHINE}.ext4 ${DEPLOY_DIR_IMAGE}/automotive-${MACHINE}-sysfs.ext4
}

# Call function makesystem to generate sparse ext4 image
IMAGE_POSTPROCESS_COMMAND   += "create_sparsesystem;"
