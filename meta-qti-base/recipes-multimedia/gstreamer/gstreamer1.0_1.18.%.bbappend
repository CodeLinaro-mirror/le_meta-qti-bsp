SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz"
SRC_URI += "${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"
SRC_URI += "${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gstreamer/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "gstreamer_common"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"

S = "${WORKDIR}/gstreamer/gstreamer"
