#add packages on top of root file system

EXTRA_IMAGE_FEATURES += "package-management"
EXTRA_IMAGE_FEATURES += "tools-debug"


IMAGE_INSTALL += "weston"
IMAGE_INSTALL += "weston-examples"
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

IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "system-core-leprop"
IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "system-core-post-boot"
IMAGE_INSTALL += "packagegroup-multimedia"
IMAGE_INSTALL += "lib32-lk"
IMAGE_INSTALL += "init-audio"
IMAGE_INSTALL += "pulseaudio-module-acdb"
IMAGE_INSTALL += "pulseaudio-module-codec-control"
IMAGE_INSTALL += "neutrino-eth"
IMAGE_INSTALL += "open-avb"
IMAGE_INSTALL += "iperf3"
IMAGE_INSTALL += "iperf"
IMAGE_INSTALL += "ethtool"
IMAGE_INSTALL += "tcpdump"
#IMAGE_INSTALL += "ebtables"
IMAGE_INSTALL += "vlan"
IMAGE_INSTALL += "bridge-utils"
IMAGE_INSTALL += "cryptsetup"
IMAGE_INSTALL += "lvm2"
IMAGE_INSTALL += "wqy-microhei"
IMAGE_INSTALL += "ltp"

#RESIZE
IMAGE_INSTALL += "resize-service"
#Firmware-links
IMAGE_INSTALL += "firmware-links"
#Camera modules
IMAGE_INSTALL += "lib32-androidcompat"
IMAGE_INSTALL += "init-can"

#WiFi
IMAGE_INSTALL += "qcacld"
IMAGE_INSTALL += "wpa-supplicant"
IMAGE_INSTALL += "hostapd"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "sigma-dut"

#BT
#IMAGE_INSTALL += "synergy-opensource"

#Location
IMAGE_INSTALL += "positioning"
IMAGE_INSTALL += "positioning-gnss"
IMAGE_INSTALL += "positioning-gnss-test"
IMAGE_INSTALL += "positioning-sns"
IMAGE_INSTALL += "positioning-sns-test"
IMAGE_INSTALL += "positioning-repl"
IMAGE_INSTALL += "positioning-repl-test"
IMAGE_INSTALL += "positioning-enhpos"
IMAGE_INSTALL += "positioning-enhpos-test"

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
IMAGE_INSTALL_append_smack = " smack-userspace"

# power management
IMAGE_INSTALL += "pm"

# AFM and Security
IMAGE_INSTALL += "af-main"
IMAGE_INSTALL += "af-binder"
IMAGE_INSTALL += "af-binder-binding-afb-dbus-binding"
IMAGE_INSTALL += "af-binder-binding-authlogin"
IMAGE_INSTALL += "libafbwsc"
IMAGE_INSTALL += "web-runtime"
IMAGE_INSTALL += "xmlsec1"
IMAGE_INSTALL += "cynara"
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

# meta-ivi tests
IMAGE_INSTALL += "common-api-test"
IMAGE_INSTALL += "dlt-daemon-test"

