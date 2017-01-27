DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and demos."

# Base image
require recipes-ivi/images/agl-image-ivi.inc
require recipes-platform/images/agl-demo-platform.bb

EXTRA_IMAGE_FEATURES += "package-management"
EXTRA_IMAGE_FEATURES += "tools-debug"

IMAGE_INSTALL += "packagegroup-agl-core"
IMAGE_INSTALL += "packagegroup-agl-ivi"
IMAGE_INSTALL += "packagegroup-ivi-common-core"
IMAGE_INSTALL += "packagegroup-agl-demo-platform"

IMAGE_INSTALL += "weston"
IMAGE_INSTALL += "weston-examples"
IMAGE_INSTALL += "qtbase-examples"
IMAGE_INSTALL += "qtwebkit"
IMAGE_INSTALL += "qtwebkit-examples-examples"
IMAGE_INSTALL += "qtmultimedia"
IMAGE_INSTALL += "qtmultimedia-plugins"
IMAGE_INSTALL += "qtmultimedia-qmlplugins"
IMAGE_INSTALL += "lmbench"
IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "packagegroup-multimedia"
#IMAGE_INSTALL += "lib32-lk"
#IMAGE_INSTALL += "lib32-gensecimage"
IMAGE_INSTALL += "cdcdriver-noship"
IMAGE_INSTALL += "audio-nxp-auto-noship"
#IMAGE_INSTALL += "init-audio"
IMAGE_INSTALL += "alsaucm-conf"
IMAGE_INSTALL += "audcal"
IMAGE_INSTALL += "acdbloader"
IMAGE_INSTALL += "pulseaudio-module-acdb"
IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "start-scripts-find-partitions"
#IMAGE_INSTALL += "neutrino-eth"
IMAGE_INSTALL += "neutrino-flash"
IMAGE_INSTALL += "neutrino-firmware"
IMAGE_INSTALL += "audio-hal-plugin-noship"
IMAGE_INSTALL += "pulseaudio-module-codec-control"
IMAGE_INSTALL += "iperf"
IMAGE_INSTALL += "ethtool"
IMAGE_INSTALL += "lib32-androidcompat"
#IMAGE_INSTALL += "lib32-cameradbg"
#IMAGE_INSTALL += "lib32-mm-camerasdk"
#IMAGE_INSTALL += "lib32-mm-camera-lib"
#IMAGE_INSTALL += "lib32-mm-camera-core"
#IMAGE_INSTALL += "lib32-mm-3a-core"
#IMAGE_INSTALL += "lib32-mm-camera"
IMAGE_INSTALL += "diag"
IMAGE_INSTALL += "qmi"
IMAGE_INSTALL += "qmi-framework"
IMAGE_INSTALL += "common"
IMAGE_INSTALL += "tftp-server"
IMAGE_INSTALL += "qmi-client-helper"
IMAGE_INSTALL += "diag-reboot-app"

#wifi
#IMAGE_INSTALL_append = " \
#    qcacld \
#    wpa-supplicant \
#    hostapd \
#    iw \
#    wireless-tools \
#"
IMAGE_ROOTFS_SIZE = "2097152"

IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
