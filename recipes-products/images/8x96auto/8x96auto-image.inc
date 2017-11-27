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

# Early init
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "early-init", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "early-init", "" ,d)}"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

###########################################################
## If WITH_INTERNAL_LAYER, enable below modules for test ##
###########################################################
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'ofono', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'ofono-tests', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'gst-multisend', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'packagegroup-agl-appfw', '',d)}"
# Add Persistence Storage related components
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'dlt-daemon', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'persistence-common-object', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'persistence-client-library', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'persistence-administrator', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-state-manager', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-health-monitor', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-startup-controller', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'node-startup-controller-nsm-dummy', '',d)}"

IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'ces2017-demo', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'can-utils', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'init-can', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'navigation', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'mediaplayer', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'settings', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'mixer', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'hvac', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'poiapp', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'dlt-daemon-test', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'common-api-c++', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'common-api-c++-dbus', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'common-api-test', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'rh850-firmware', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'rh850-flash', '',d)}"
IMAGE_INSTALL_remove += "packagegroup-agl-demo-platform"
IMAGE_INSTALL_remove += "neutrino-eth"
IMAGE_INSTALL_remove += "web-runtime"



