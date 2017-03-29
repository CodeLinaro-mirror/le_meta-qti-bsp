DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and \
demos."

require recipes-platform/images/agl-demo-platform.bb
require ../common-auto-image.inc


IMAGE_INSTALL_remove = "neutrino-eth"
IMAGE_INSTALL_remove = "open-avb"
IMAGE_INSTALL_remove = "ebtables"
IMAGE_INSTALL_remove = "qcacld"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
