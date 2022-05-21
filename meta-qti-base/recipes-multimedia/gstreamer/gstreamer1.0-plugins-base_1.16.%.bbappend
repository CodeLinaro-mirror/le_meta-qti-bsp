DEFAULT_PREFERENCE = "-1"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-base/gst-plugins-base-${PV}.tar.xz"
SRC_URI += "${PATH_TO_REPO}/gstreamer/gst-plugins-base/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-base;usehead=1"
SRC_URI += "${CLO_LE_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-base/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"
SRCREV_FORMAT = "base_common"

S = "${WORKDIR}/gstreamer/gst-plugins-base"

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'alsa x11', d)} \
    jpeg-turbo ogg pango png theora vorbis \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland egl', '', d)} \
"

DEPENDS += "libcutils"
GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
