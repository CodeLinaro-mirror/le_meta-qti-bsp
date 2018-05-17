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
IMAGE_INSTALL += "protobuf"

#BT
IMAGE_INSTALL += "${@base_conditional('SYNERGY_DIR_PRESENT', 'True', 'synergy-opensource', '', d)}"

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
# ### IMAGE_INSTAL_WITH_INTERNAL_LAYER ###
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "ofono ofono-tests"
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "gst-multisend"
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "packagegroup-agl-appfw"
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "mm-vdec-omx-test mm-venc-omx-test"
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "expect"
# Add Persistence Storage related components
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "dlt-daemon"
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "persistence-common-object persistence-client-library persistence-administrator"
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "node-state-manager node-health-monitor node-startup-controller node-startup-controller-nsm-dummy"
# QT
IMAGE_INSTAL_WITH_INTERNAL_LAYER += "qtbase-examples qtmultimedia qtmultimedia-plugins qtmultimedia-qmlplugins"
#IMAGE_INSTAL_WITH_INTERNAL_LAYER += "qtwebkit qtwebkit-examples-examples qtwebengine-qmlplugins qtwebengine-examples qtwebsockets qtwebchannel"
IMAGE_INSTALL += "${@base_conditional('WITH_INTERNAL_LAYER', 'yes', IMAGE_INSTAL_WITH_INTERNAL_LAYER, '',d)}"

# ### IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER ###
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "ces2017-demo can-utils init-can navigation weston-examples"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "mediaplayer settings mixer hvac poiapp"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "dlt-daemon-test common-api-test"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "rh850-firmware rh850-flash"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "packagegroup-ivi-common-core packagegroup-agl-core"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "pulseaudio-module-acdb pulseaudio-module-codec-control"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "iperf wqy-microhei wpa-supplicant qcacld-utils qcacld hostapd sigma-dut strongswan mm-rtp-test ath6kl-utils ftm"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "ttf-bitstream-vera ttf-dejavu-sans ttf-dejavu-sans-mono ttf-dejavu-serif af-main af-binder af-binder-binding-afb-dbus-binding af-binder-binding-authlogin"
IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "libafbwsc"
#IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER += "web-runtime"
IMAGE_INSTALL_remove += "${@base_conditional('WITH_INTERNAL_LAYER', 'no', IMAGE_INSTAL_REMOVE_WITH_INTERNAL_LAYER, '',d)}"


#Always remove below modules
IMAGE_INSTALL_remove += "firmware-links"
IMAGE_INSTALL_remove += "packagegroup-agl-demo-platform"
IMAGE_INSTALL_remove += "linux-firmware-ath9k linux-firmware-ralink"
IMAGE_INSTALL_remove += "libhardware-legacy qmi-client-helper"
IMAGE_INSTALL_remove += "web-runtime"

IMAGE_INSTALL_remove += "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", "neutrino-eth", "" ,d)}"
