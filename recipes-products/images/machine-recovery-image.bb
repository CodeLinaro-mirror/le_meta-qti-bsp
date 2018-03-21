# List of packages installed onto the root file system as specified by the user.
inherit module qperf

#LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS = "virtual/kernel"

include ${MACHINE}/${MACHINE}-recovery-image.inc
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
PACKAGE_INSTALL = "${IMAGE_INSTALL}"

IMAGE_LINGUAS = ""

# Use busybox as login manager
IMAGE_LOGIN_MANAGER = "busybox-static"

# Include minimum init and init scripts
IMAGE_DEV_MANAGER ?= "busybox-static-mdev"
IMAGE_INIT_MANAGER ?= "sysvinit sysvinit-pidof"
IMAGE_INITSCRIPTS ?= ""

inherit core-image
inherit deploy
#inherit kernel

include ${MACHINE}/${MACHINE}-ota-target-image-ext4.inc

addtask makerecovery after do_rootfs before do_build
addtask makeota_image after do_makerecovery before do_build
do_makerecovery[depends] += "virtual/kernel:do_shared_workdir"

# For non "prop" layer settings.
IMAGE_INSTALL_remove += "${@base_conditional('WITH_PROP_LAYER', 'no', 'adreno-recovery', '',d)}"

