DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and \
demos."

require recipes-platform/images/agl-demo-platform.bb

#add packages on top of AGL root file system

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
IMAGE_INSTALL += "lib32-lk"
IMAGE_INSTALL += "lib32-gensecimage"
IMAGE_INSTALL += "cdcdriver-noship"
IMAGE_INSTALL += "audio-nxp-auto-noship"
IMAGE_INSTALL += "init-audio"
IMAGE_INSTALL += "alsaucm-conf"
IMAGE_INSTALL += "audcal"
IMAGE_INSTALL += "acdbloader"
IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "start-scripts-find-partitions"
IMAGE_INSTALL += "neutrino-eth"
IMAGE_INSTALL += "neutrino-flash"
IMAGE_INSTALL += "neutrino-firmware"
IMAGE_INSTALL += "iperf"
IMAGE_INSTALL += "ethtool"
IMAGE_INSTALL += "lib32-androidcompat"
IMAGE_INSTALL += "lib32-cameradbg"
IMAGE_INSTALL += "lib32-mm-camerasdk"
IMAGE_INSTALL += "lib32-mm-camera-lib"
IMAGE_INSTALL += "lib32-mm-camera-core"
IMAGE_INSTALL += "lib32-mm-3a-core"
IMAGE_INSTALL += "lib32-mm-camera"
IMAGE_INSTALL += "diag"
IMAGE_INSTALL += "qmi"
IMAGE_INSTALL += "qmi-framework"
IMAGE_INSTALL += "common"
IMAGE_INSTALL += "tftp-server"


#wifi
IMAGE_INSTALL_append = " \
    qcacld \
    wpa-supplicant \
    hostapd \
    iw \
    wireless-tools \
"

IMAGE_ROOTFS_SIZE = "1048576"

IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
