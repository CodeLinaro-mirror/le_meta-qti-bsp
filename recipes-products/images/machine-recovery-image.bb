include target/${BASEMACHINE}-recovery-image.inc

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

include target/${BASEMACHINE}-ota-target-image-ext4.inc

addtask makerecovery after do_rootfs before do_build
addtask makeota_image after do_makerecovery before do_build
