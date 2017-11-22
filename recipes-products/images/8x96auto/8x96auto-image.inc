DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and \
demos."

require recipes-platform/images/agl-demo-platform.bb
require ../common-auto-image.inc

IMAGE_INSTALL += "sqlite3"
IMAGE_INSTALL += "system-core"
IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "native-frameworks"
IMAGE_INSTALL += "libhardware"
IMAGE_INSTALL += "libhardware-legacy"
IMAGE_INSTALL += "libcap"
IMAGE_INSTALL += "init-mss"
IMAGE_INSTALL += "strongswan"
IMAGE_INSTALL += "reboot-daemon"

IMAGE_INSTALL_remove = "packagegroup-agl-demo-platform"
IMAGE_INSTALL += "computelibrary"
IMAGE_INSTALL_remove += "neutrino-eth"

IMAGE_INSTALL_remove = "qtwebengine"
IMAGE_INSTALL_remove = "qtwebengine-qmlplugins"
IMAGE_INSTALL_remove = "qtwebengine-examples"
IMAGE_INSTALL_remove = "qtwebsockets"
IMAGE_INSTALL_remove = "qtwebchannel"
IMAGE_INSTALL_remove = "web-runtime"

# Early init
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "early-init", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "early-init", "" ,d)}"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

IMAGE_INSTALL_remove = "ces2017-demo"
IMAGE_INSTALL_remove = "can-utils"
IMAGE_INSTALL_remove = "init-can"
IMAGE_INSTALL_remove = "navigation"
IMAGE_INSTALL_remove = "mediaplayer"
IMAGE_INSTALL_remove = "settings"
IMAGE_INSTALL_remove = "mixer"
IMAGE_INSTALL_remove = "hvac"
IMAGE_INSTALL_remove = "poiapp"
IMAGE_INSTALL_remove = "packagegroup-agl-appfw"
IMAGE_INSTALL_remove = "dlt-daemon"
IMAGE_INSTALL_remove = "dlt-daemon-test"
IMAGE_INSTALL_remove = "persistence-administrator"
IMAGE_INSTALL_remove = "persistence-client-library"
IMAGE_INSTALL_remove = "persistence-common-object"
IMAGE_INSTALL_remove = "node-startup-controller"
IMAGE_INSTALL_remove = "node-startup-controller-nsm-dummy"
IMAGE_INSTALL_remove = "node-state-manager"
IMAGE_INSTALL_remove = "node-health-monitor"
IMAGE_INSTALL_remove = "common-api-c++"
IMAGE_INSTALL_remove = "common-api-c++-dbus"
IMAGE_INSTALL_remove = "common-api-test"
IMAGE_INSTALL_remove = "rh850-firmware"
IMAGE_INSTALL_remove = "rh850-flash"



