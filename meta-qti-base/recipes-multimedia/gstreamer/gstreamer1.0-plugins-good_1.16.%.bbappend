LIC_FILES_CHKSUM_remove = "file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607"
SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-good/gst-plugins-good-${PV}.tar.xz"
SRC_URI_append = " ${PATH_TO_REPO}/gstreamer/gst-plugins-good/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-good;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gst-plugins-good"
