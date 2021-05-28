DEFAULT_PREFERENCE = "-1"

PACKAGECONFIG ??= "orc opencore-amr"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz"
SRC_URI += "${PATH_TO_REPO}/gstreamer/gst-plugins-ugly/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-ugly;usehead=1"
SRC_URI += "${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-ugly/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "ugly_common"

S = "${WORKDIR}/gstreamer/gst-plugins-ugly"


