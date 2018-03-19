#add packages on top of root file system

require recipes-platform/images/agl-demo-platform.bb
require 8x96autogvmquintcu-image-check-size.bb
EXTRA_IMAGECMD_ext4="-i 4096 -b 4096"

EXTRA_IMAGE_FEATURES += "package-management"
#EXTRA_IMAGE_FEATURES += "tools-debug"
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
#IMAGE_INSTALL += "start-scripts-find-partitions"
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
IMAGE_INSTALL += "qahw"
IMAGE_INSTALL += "audiohal"

#Camera modules
#IMAGE_INSTALL += "lib32-androidcompat"
#IMAGE_INSTALL += "init-can"

#WiFi
IMAGE_INSTALL += "qcacld"
IMAGE_INSTALL += "qcacld-dsrc"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "hostapd"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "sigma-dut"
IMAGE_INSTALL += "wldsrc-tools"

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

#IMAGE_INSTALL += "usbutils"
#IMAGE_INSTALL += "sysstat"
#IMAGE_INSTALL += "glib-2.0-utils"
#IMAGE_INSTALL += "smem"

# smack support
#IMAGE_INSTALL_append_smack = " smack-userspace"

# AFM and Security
IMAGE_INSTALL_remove += "af-main"
IMAGE_INSTALL_remove += "af-binder"
IMAGE_INSTALL_remove += "af-binder-binding-afb-dbus-binding"
IMAGE_INSTALL_remove += "af-binder-binding-authlogin"
IMAGE_INSTALL_remove += "libafbwsc"
IMAGE_INSTALL_remove += "web-runtime"
IMAGE_INSTALL_remove += "xmlsec1"
IMAGE_INSTALL_remove += "cynara"
IMAGE_INSTALL_remove += "security-manager"
IMAGE_INSTALL_remove += "security-manager-policy"
IMAGE_INSTALL_remove += "agl-users"

#Location
IMAGE_INSTALL += "loc-stub"
IMAGE_INSTALL += "loc-pla"
IMAGE_INSTALL += "location-api-iface"
IMAGE_INSTALL += "gps-utils"
IMAGE_INSTALL += "loc-hal"
IMAGE_INSTALL += "loc-net-iface"

# modem pass through
IMAGE_INSTALL += "ril"
#IMAGE_INSTALL += "sqlite3"
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

# setup network for tcu
IMAGE_INSTALL += "setup-tcu-network"

IMAGE_ROOTFS_SIZE = "323584"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

USERDATA_SIZE_EXT4 = "256M"
PERSIST_SIZE_EXT4  = "16M"

IMAGE_INSTALL_remove += "pulseaudio-module-codec-control"
IMAGE_INSTALL_remove += "pulseaudio-module-acdb"

IMAGE_INSTALL_remove += "qtmultimedia"
IMAGE_INSTALL_remove += "poiapp"
IMAGE_INSTALL_remove += "mediaplayer"
IMAGE_INSTALL_remove += "ces2017-demo"
IMAGE_INSTALL_remove += "settings"
IMAGE_INSTALL_remove += "mixer"
IMAGE_INSTALL_remove += "hvac"
IMAGE_INSTALL_remove += "gstreamer1.0-qvconv"
IMAGE_INSTALL_remove += "packagegroup-agl-demo-platform"
IMAGE_INSTALL_remove += "homescreen"
IMAGE_INSTALL_remove += "af-binder"
IMAGE_INSTALL_remove += "positioning"
IMAGE_INSTALL_remove += "navigation"
IMAGE_INSTALL_remove += "can-lin"
IMAGE_INSTALL_remove += "vod-server"
IMAGE_INSTALL_remove += "unicens"
IMAGE_INSTALL_remove += "packagegroup-agl-appfw-native-qt5"
IMAGE_INSTALL_remove += "packagegroup-ivi-common-core-graphics"
IMAGE_INSTALL_remove += "weston"
IMAGE_INSTALL_remove += "weston-examples"
IMAGE_INSTALL_remove += "wqy-microhei"
IMAGE_INSTALL_remove += "dbus"
IMAGE_INSTALL_remove += "dbus-glib"

IMAGE_INSTALL_remove += "dlt-daemon"
IMAGE_INSTALL_remove += "persistence-common-object"
IMAGE_INSTALL_remove += "persistence-client-library"
IMAGE_INSTALL_remove += "persistence-administrator"
IMAGE_INSTALL_remove += "node-state-manager"
IMAGE_INSTALL_remove += "node-health-monitor"
IMAGE_INSTALL_remove += "node-startup-controller"
IMAGE_INSTALL_remove += "node-startup-controller-nsm-dummy"
IMAGE_INSTALL_remove += "usbutils"
IMAGE_INSTALL_remove += "sysstat"
IMAGE_INSTALL_remove += "glib-2.0-utils"
IMAGE_INSTALL_remove += "smem"
IMAGE_INSTALL_remove += "common-api-c++-dbus"
IMAGE_INSTALL_remove += "common-api-c++"
IMAGE_INSTALL_remove += "sqlite"
IMAGE_INSTALL_remvoe += "sqlite-bin"
IMAGE_INSTALL_remove += "common-api-test"
IMAGE_INSTALL_remove += "dlt-daemon-test"
IMAGE_INSTALL_remove += "sdm"


IMAGE_INSTALL_remove_smack = " smack-userspace"


EXTRA_IMAGE_FEATURES_remove += "tools-debug"


