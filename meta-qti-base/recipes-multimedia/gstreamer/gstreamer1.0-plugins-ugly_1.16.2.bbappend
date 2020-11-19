DEFAULT_PREFERENCE = "-1"

DEPENDS += "opencore-amr"

PACKAGECONFIG ??= "orc opencore-amr"
DEPENDS += "opencore-amr"
SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-ugly/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-ugly;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-ugly/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "ugly_common"

S = "${WORKDIR}/gstreamer/gst-plugins-ugly"


EXTRA_OEMESON += " \
    -Da52dec=disabled \
    -Dcdio=disabled \
    -Ddvdlpcmdec=disabled \
    -Ddvdread=disabled \
    -Ddvdsub=disabled \
    -Dmpeg2dec=disabled \
    -Drealmedia=disabled \
    -Dsidplay=disabled \
    -Dx264=disabled \
    -Dxingmux=disabled \
    -Damrnb=enabled \
    -Damrwbdec=enabled \
    -Dasfdemux=enabled \
    "

