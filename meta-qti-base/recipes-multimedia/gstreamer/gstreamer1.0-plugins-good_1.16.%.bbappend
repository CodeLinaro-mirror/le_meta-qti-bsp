DEFAULT_PREFERENCE = "-1"

SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gst-plugins-good/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-good;usehead=1"
SRC_URI_append = " ${CLO_LE_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-good/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"
SRCREV_FORMAT = "good_common"

S = "${WORKDIR}/gstreamer/gst-plugins-good"

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'pulseaudio x11', d)} \
    bz2 cairo flac gdk-pixbuf gudev jpeg-turbo lame libpng mpg123 soup speex taglib v4l2 \
"
PACKAGECONFIG[v4l2]       = "-Dv4l2=enabled -Dv4l2-probe=false,-Dv4l2=false"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

RPROVIDES_${PN}-souphttpsrc = "${PN}-soup"
