# List of packages installed onto the root file system as specified by the user.

DEPENDS = "virtual/kernel"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
PACKAGE_INSTALL = "${IMAGE_INSTALL}"

IMAGE_LINGUAS = ""

# Use busybox as login manager
IMAGE_LOGIN_MANAGER = "busybox-static"

# Include minimum init and init scripts
IMAGE_DEV_MANAGER ?= "busybox-static-mdev"
IMAGE_INIT_MANAGER = "sysvinit sysvinit-pidof"
IMAGE_INITSCRIPTS ?= ""

inherit core-image
inherit deploy

IMAGE_INSTALL += "makedumpfile"
IMAGE_INSTALL += "kexec-tools"
