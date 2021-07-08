SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz"
SRC_URI += "${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"
SRC_URI += "${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gstreamer/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "gstreamer_common"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"

S = "${WORKDIR}/gstreamer/gstreamer"
