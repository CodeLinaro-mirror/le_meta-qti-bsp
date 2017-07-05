DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and demos."

# Base image
require recipes-ivi/images/agl-image-ivi.inc
require ../common-auto-image.inc

IMAGE_INSTALL += "packagegroup-agl-core"
IMAGE_INSTALL += "packagegroup-agl-ivi"
IMAGE_INSTALL += "packagegroup-ivi-common-core"
# Remove HomeScreen from mctm
IMAGE_INSTALL += "packagegroup-agl-appfw"
# IMAGE_INSTALL += "packagegroup-agl-demo-platform"
IMAGE_INSTALL += "gstreamer1.0-mprtp"
IMAGE_INSTALL += "sofia-sip"
IMAGE_INSTALL += "iproute2"
IMAGE_INSTALL += "multimodule-mgr-oss"
IMAGE_INSTALL += "strongswan"
IMAGE_INSTALL += "start-scripts-check-firmware-corruption"

IMAGE_INSTALL_remove += "synergy-opensource"
IMAGE_INSTALL_remove += "bluez5"
IMAGE_INSTALL_remove += "init-can"

IMAGE_INSTALL_remove += "qtwebengine"
IMAGE_INSTALL_remove += "qtwebengine-qmlplugins"
IMAGE_INSTALL_remove += "qtwebengine-examples"
IMAGE_INSTALL_remove += "qtwebsockets"
IMAGE_INSTALL_remove += "qtwebchannel"



IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
