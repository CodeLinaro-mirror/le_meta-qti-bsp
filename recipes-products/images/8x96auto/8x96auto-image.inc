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
#IMAGE_INSTALL += "init-mss"
IMAGE_INSTALL += "strongswan"
IMAGE_INSTALL += "reboot-daemon"

IMAGE_INSTALL += "computelibrary"
IMAGE_INSTALL_remove += "neutrino-eth"

IMAGE_INSTALL += "qtbase-examples"
IMAGE_INSTALL += "qtwebkit"
IMAGE_INSTALL += "qtwebkit-examples-examples"
IMAGE_INSTALL += "qtmultimedia"
IMAGE_INSTALL += "qtmultimedia-plugins"
IMAGE_INSTALL += "qtmultimedia-qmlplugins"
IMAGE_INSTALL += "qtwebengine"
IMAGE_INSTALL += "qtwebengine-qmlplugins"
IMAGE_INSTALL += "qtwebengine-examples"
IMAGE_INSTALL += "qtwebsockets"
IMAGE_INSTALL += "qtwebchannel"


# Perf
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "memory-hotplug", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "memory-hotplug", "" ,d)}"

# Early init
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "early-init", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "early-init", "" ,d)}"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"


IMAGE_INSTALL += "mm-vdec-omx-test"
IMAGE_INSTALL += "mm-venc-omx-test"

