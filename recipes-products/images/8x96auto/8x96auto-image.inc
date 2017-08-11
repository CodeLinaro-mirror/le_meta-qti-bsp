DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and \
demos."

require recipes-platform/images/agl-demo-platform.bb
require ../common-auto-image.inc

IMAGE_INSTALL += "ril"
IMAGE_INSTALL += "sqlite3"
IMAGE_INSTALL += "system-core"
IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "native-frameworks"
IMAGE_INSTALL += "protobuf"
IMAGE_INSTALL += "libhardware"
IMAGE_INSTALL += "libhardware-legacy"
IMAGE_INSTALL += "libcap"
IMAGE_INSTALL += "ofono"
IMAGE_INSTALL += "ofono-tests"
IMAGE_INSTALL += "init-mss"
IMAGE_INSTALL += "strongswan"
IMAGE_INSTALL += "gst-multisend"

IMAGE_INSTALL_remove = "packagegroup-agl-demo-platform"
IMAGE_INSTALL += "packagegroup-agl-appfw"
IMAGE_INSTALL_remove += "neutrino-eth"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
