SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz"
SRC_URI_append = " ${PATH_TO_REPO}/gstreamer/gst-plugins-ugly/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-ugly;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-ugly/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"
SRCREV_FORMAT = "ugly_common"

S = "${WORKDIR}/gstreamer/gst-plugins-ugly"

# remove a52dec and mpeg2dec, for don't support.
PACKAGECONFIG_remove = "a52dec mpeg2dec"
