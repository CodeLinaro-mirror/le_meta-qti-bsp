# List of FOSS packages installed onto the root file system as specified by the user.

# Base image
require recipes-ivi/images/agl-image-ivi.inc
require common-auto-image.inc

IMAGE_INSTALL += "packagegroup-agl-core"
IMAGE_INSTALL += "packagegroup-agl-ivi"
IMAGE_INSTALL += "packagegroup-ivi-common-core"
IMAGE_INSTALL += "packagegroup-agl-demo-platform"
IMAGE_INSTALL += "gstreamer1.0-mprtp"
IMAGE_INSTALL += "sofia-sip"
IMAGE_INSTALL += "iproute2"

IMAGE_INSTALL += "iproute2"

IMAGE_INSTALL += "multimodule-mgr-oss"


IMAGE_ROOTFS_SIZE = "2097152"

IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
