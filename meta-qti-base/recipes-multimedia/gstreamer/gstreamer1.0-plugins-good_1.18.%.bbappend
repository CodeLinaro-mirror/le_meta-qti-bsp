SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-good/gst-plugins-good-${PV}.tar.xz"
SRC_URI += "${PATH_TO_REPO}/gstreamer/gst-plugins-good/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-good;usehead=1"
SRC_URI += "${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-good/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "good_common"

S = "${WORKDIR}/gstreamer/gst-plugins-good"
