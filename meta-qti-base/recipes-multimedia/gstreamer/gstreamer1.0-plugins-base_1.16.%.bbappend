LIC_FILES_CHKSUM_remove = "file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607"

# add depends of libion, libsync, libuhab for HY11 build error
DEPENDS += "libcutils libion libsync"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-base/gst-plugins-base-${PV}.tar.xz"
SRC_URI_append = " ${PATH_TO_REPO}/gstreamer/gst-plugins-base/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-base;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gst-plugins-base"
