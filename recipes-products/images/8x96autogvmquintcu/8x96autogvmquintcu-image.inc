#add packages on top of root file system

require recipes-platform/images/agl-demo-platform.bb

EXTRA_IMAGE_FEATURES += "package-management"
EXTRA_IMAGE_FEATURES += "tools-debug"
EXTRA_IMAGE_FEATURES += "ssh-server-openssh"

#IMAGE_INSTALL += "packagegroup-agl-core"
#IMAGE_INSTALL += "packagegroup-agl-ivi"
#IMAGE_INSTALL += "packagegroup-ivi-common-core"
#IMAGE_INSTALL += "packagegroup-agl-demo-platform"

#IMAGE_INSTALL += "weston"
#IMAGE_INSTALL += "weston-examples"
#IMAGE_INSTALL += "qtbase-examples"
#IMAGE_INSTALL += "qtwebkit"
#IMAGE_INSTALL += "qtwebkit-examples-examples"
#IMAGE_INSTALL += "qtmultimedia"
#IMAGE_INSTALL += "qtmultimedia-plugins"
#IMAGE_INSTALL += "qtmultimedia-qmlplugins"
#IMAGE_INSTALL += "lmbench"
IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "system-core-leprop"
IMAGE_INSTALL += "system-prop"
#IMAGE_INSTALL += "packagegroup-multimedia"
#IMAGE_INSTALL += "lib32-lk"
IMAGE_INSTALL += "init-audio"
IMAGE_INSTALL += "pulseaudio-module-acdb"
IMAGE_INSTALL += "pulseaudio-module-codec-control"
IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "start-scripts-find-partitions"
#IMAGE_INSTALL += "neutrino-eth"
#IMAGE_INSTALL += "open-avb"
IMAGE_INSTALL += "iperf3"
IMAGE_INSTALL += "iperf"
IMAGE_INSTALL += "ethtool"
IMAGE_INSTALL += "tcpdump"
#IMAGE_INSTALL += "ebtables"
IMAGE_INSTALL += "vlan"
IMAGE_INSTALL += "bridge-utils"
IMAGE_INSTALL += "cryptsetup"
IMAGE_INSTALL += "lvm2"
IMAGE_INSTALL += "pciutils"
IMAGE_INSTALL += "audiohal"

#Camera modules
#IMAGE_INSTALL += "lib32-androidcompat"
#IMAGE_INSTALL += "init-can"

#WiFi
IMAGE_INSTALL += "qcacld"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "hostapd"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "sigma-dut"

#BT
#IMAGE_INSTALL += "synergy-opensource"

# Add Persistence Storage related components
#IMAGE_INSTALL += "dlt-daemon"
#IMAGE_INSTALL += "persistence-common-object"
#IMAGE_INSTALL += "persistence-client-library"
#IMAGE_INSTALL += "persistence-administrator"
#IMAGE_INSTALL += "node-state-manager"
#IMAGE_INSTALL += "node-health-monitor"
#IMAGE_INSTALL += "node-startup-controller"

IMAGE_INSTALL += "usbutils"
IMAGE_INSTALL += "sysstat"
IMAGE_INSTALL += "glib-2.0-utils"
IMAGE_INSTALL += "smem"

# smack support
#IMAGE_INSTALL_append_smack = " smack-userspace"

# AFM and Security
#IMAGE_INSTALL += "af-main"
#IMAGE_INSTALL += "af-binder"
#IMAGE_INSTALL += "af-binder-binding-afb-dbus-binding"
#IMAGE_INSTALL += "af-binder-binding-authlogin"
#IMAGE_INSTALL += "libafbwsc"
#IMAGE_INSTALL += "web-runtime"
#IMAGE_INSTALL += "xmlsec1"
#IMAGE_INSTALL += "cynara"
#IMAGE_INSTALL += "security-manager"
#IMAGE_INSTALL += "security-manager-policy"
#IMAGE_INSTALL += "agl-users"

#Location
IMAGE_INSTALL += "loc-stub"
IMAGE_INSTALL += "loc-pla"
IMAGE_INSTALL += "location-api-iface"
IMAGE_INSTALL += "gps-utils"
IMAGE_INSTALL += "loc-hal"
IMAGE_INSTALL += "loc-net-iface"

# modem pass through
IMAGE_INSTALL += "ril"
IMAGE_INSTALL += "sqlite3"
IMAGE_INSTALL += "system-core"
IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "native-frameworks"
IMAGE_INSTALL += "protobuf"
IMAGE_INSTALL += "libhardware"
IMAGE_INSTALL += "libhardware-legacy"
IMAGE_INSTALL += "libcap"
#IMAGE_INSTALL += "ofono"
#IMAGE_INSTALL += "ofono-tests"
IMAGE_INSTALL += "init-mss"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

IMAGE_INSTALL_remove += "pulseaudio-module-codec-control"
