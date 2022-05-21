DEFAULT_PREFERENCE = "-1"

DEPENDS += "opencore-amr"

PACKAGECONFIG ??= "orc opencore-amr"
DEPENDS += "opencore-amr"
SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-ugly/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-ugly;usehead=1"
SRC_URI_append = " ${CLO_LE_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-ugly/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"
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

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

