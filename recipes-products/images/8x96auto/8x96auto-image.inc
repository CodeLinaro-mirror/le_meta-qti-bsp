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

IMAGE_INSTALL += "computelibrary"

# Perf
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "qti-perf", "memory-hotplug", "" ,d)}"

# Early init
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "early-init", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "early-init", "" ,d)}"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"


IMAGE_INSTALL += "ofono"
IMAGE_INSTALL += "ofono-tests"
IMAGE_INSTALL += "gst-multisend"
# Add Persistence Storage related components
IMAGE_INSTALL += "dlt-daemon"
IMAGE_INSTALL += "persistence-common-object"
IMAGE_INSTALL += "persistence-client-library"
IMAGE_INSTALL += "persistence-administrator"
IMAGE_INSTALL += "node-state-manager"
IMAGE_INSTALL += "node-health-monitor"
IMAGE_INSTALL += "node-startup-controller"
IMAGE_INSTALL += "node-startup-controller-nsm-dummy"



