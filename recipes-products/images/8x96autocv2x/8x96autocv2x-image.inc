DESCRIPTION = "AGL image for CV2X fusion headless board"

# Base image
require recipes-ivi/images/agl-image-ivi.inc

IMAGE_INSTALL += "packagegroup-agl-core"

IMAGE_INSTALL += "iproute2"
IMAGE_INSTALL += "pciutils"
IMAGE_INSTALL += "spitools"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

# From the list of common automotive packages

EXTRA_IMAGE_FEATURES += "package-management"
EXTRA_IMAGE_FEATURES += "tools-debug"

IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "system-core-leprop"
IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "system-core-post-boot"
IMAGE_INSTALL += "lib32-lk"
IMAGE_INSTALL += "neutrino-eth"
IMAGE_INSTALL += "iperf3"
IMAGE_INSTALL += "iperf"
IMAGE_INSTALL += "ethtool"
IMAGE_INSTALL += "tcpdump"
IMAGE_INSTALL += "cryptsetup"
IMAGE_INSTALL += "lvm2"
IMAGE_INSTALL += "init-can"

#RESIZE
IMAGE_INSTALL += "resize-service"
#Firmware-links
IMAGE_INSTALL += "firmware-links"

#WiFi
IMAGE_INSTALL += "qcacld"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "hostapd"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "sigma-dut"

IMAGE_INSTALL += "dbus"
IMAGE_INSTALL += "dbus-glib"

# Add Persistence Storage related components
IMAGE_INSTALL += "dlt-daemon"
IMAGE_INSTALL += "persistence-common-object"
IMAGE_INSTALL += "persistence-client-library"
IMAGE_INSTALL += "persistence-administrator"
IMAGE_INSTALL += "node-state-manager"
IMAGE_INSTALL += "node-health-monitor"
IMAGE_INSTALL += "node-startup-controller"
IMAGE_INSTALL += "node-startup-controller-nsm-dummy"

IMAGE_INSTALL += "usbutils"
IMAGE_INSTALL += "sysstat"
IMAGE_INSTALL += "glib-2.0-utils"
IMAGE_INSTALL += "smem"

# smack support
IMAGE_INSTALL += "smack-userspace"

# power management
IMAGE_INSTALL += "pm"

# AFM and Security
IMAGE_INSTALL += "af-main"
IMAGE_INSTALL += "af-binder"
IMAGE_INSTALL += "af-binder-binding-afb-dbus-binding"
IMAGE_INSTALL += "af-binder-binding-authlogin"
IMAGE_INSTALL += "libafbwsc"
IMAGE_INSTALL += "xmlsec1"
IMAGE_INSTALL += "cynara"
IMAGE_INSTALL += "dbus-cynara"
IMAGE_INSTALL += "security-manager"
IMAGE_INSTALL += "security-manager-policy"
IMAGE_INSTALL += "agl-users"

# base passwd
IMAGE_INSTALL += "base-passwd"

# GENIVI common-api
IMAGE_INSTALL += "common-api-c++-dbus"
IMAGE_INSTALL += "common-api-c++"

# Sqlite
IMAGE_INSTALL += "sqlite"
IMAGE_INSTALL += "sqlite-bin"

# libcap
IMAGE_INSTALL += "libcap"
IMAGE_INSTALL += "libcap-bin"
