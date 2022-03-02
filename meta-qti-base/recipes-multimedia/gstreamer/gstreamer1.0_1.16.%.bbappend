SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz"
SRC_URI_append = " ${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gstreamer"
