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
IMAGE_INSTALL += "ofono"
IMAGE_INSTALL += "ofono-tests"
IMAGE_INSTALL += "telephony-service"
IMAGE_INSTALL += "packagegroup-ros-qti"
IMAGE_INSTALL += "libatomic"
IMAGE_INSTALL += "samba"
IMAGE_INSTALL += "cifs-utils"

IMAGE_INSTALL_remove += "pulseaudio-module-acdb"
IMAGE_INSTALL_remove += "pulseaudio-module-codec-control"

IMAGE_ROOTFS_SIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
