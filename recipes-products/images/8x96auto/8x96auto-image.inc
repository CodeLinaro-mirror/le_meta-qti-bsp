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
IMAGE_INSTALL += "procps"

# Perf
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early_init", "memory-hotplug", "" ,d)}"
IMAGE_INSTALL += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "memory-hotplug", "" ,d)}"

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
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'mm-vdec-omx-test', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'mm-venc-omx-test', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'expect', '',d)}"

# Qt
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtbase-examples', '',d)}"
#IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtwebkit', '',d)}"
#IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtwebkit-examples-examples', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtmultimedia', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtmultimedia-plugins', '',d)}"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtmultimedia-qmlplugins', '',d)}"
#IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtwebengine-qmlplugins', '',d)}"
#IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtwebengine-examples', '',d)}"
#IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtwebsockets', '',d)}"
#IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', 'qtwebchannel', '',d)}"

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
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'common-api-test', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'rh850-firmware', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'rh850-flash', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'packagegroup-ivi-common-core', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'weston-examples', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'pulseaudio-module-acdb', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'pulseaudio-module-codec-control', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'iperf', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'wqy-microhei', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'wpa-supplicant', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'packagegroup-agl-core', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'qcacld-utils', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'qcacld', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'hostapd', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'sigma-dut', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'strongswan', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'mm-rtp-test', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'ath6kl-utils', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'ftm', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'ttf-bitstream-vera', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'ttf-dejavu-sans', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'ttf-dejavu-sans-mono', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'ttf-dejavu-serif', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'af-main', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'af-binder', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'af-binder-binding-afb-dbus-binding', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'af-binder-binding-authlogin', '',d)}"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'libafbwsc', '',d)}"
#IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', 'web-runtime', '',d)}"

#Always remove below modules
IMAGE_INSTALL_remove += "firmware-links"
IMAGE_INSTALL_remove += "packagegroup-agl-demo-platform"
IMAGE_INSTALL_remove += "linux-firmware-ath9k linux-firmware-ralink"
IMAGE_INSTALL_remove += "libhardware-legacy qmi-client-helper"
IMAGE_INSTALL_remove += "web-runtime"

IMAGE_INSTALL_remove += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "neutrino-eth", "" ,d)}"
