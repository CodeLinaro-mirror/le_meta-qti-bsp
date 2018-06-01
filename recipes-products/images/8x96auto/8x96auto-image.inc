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

#IMAGE_INSTALL_remove = "qtwebengine"
#IMAGE_INSTALL_remove = "qtwebengine-qmlplugins"
#IMAGE_INSTALL_remove = "qtwebengine-examples"
#IMAGE_INSTALL_remove = "qtwebsockets"
#IMAGE_INSTALL_remove = "qtwebchannel"

# Perf
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "memory-hotplug", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "memory-hotplug", "" ,d)}"

# Early init
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "early-init", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "early-init", "" ,d)}"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

# Add Persistence Storage related components
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'dlt-daemon', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'persistence-common-object', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'persistence-client-library', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'persistence-administrator', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-state-manager', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-health-monitor', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-startup-controller', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-startup-controller-nsm-dummy', '',d)}"


IMAGE_INSTALL += "mm-vdec-omx-test"
IMAGE_INSTALL += "mm-venc-omx-test"

