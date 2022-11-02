# add depends of libion, libsync, libuhab for HY11 build error
DEPENDS += "libcutils libion libsync"
DEPENDS += "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', 'libdmabufheap', '', d)}"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"

SRC_URI:remove = "https://gstreamer.freedesktop.org/src/gst-plugins-base/gst-plugins-base-${PV}.tar.xz"
SRC_URI:append = " ${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gstreamer/subprojects/gst-plugins-base"
