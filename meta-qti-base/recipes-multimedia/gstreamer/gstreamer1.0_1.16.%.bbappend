DEFAULT_PREFERENCE = "-1"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz"
SRC_URI += "${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"
SRC_URI += "${CLO_LE_GIT}/gstreamer/common;destsuffix=gstreamer/gstreamer/common;nobranch=1;name=common"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "gstreamer_common"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
S = "${WORKDIR}/gstreamer/gstreamer"

DEPENDS = "gobject-introspection bison-native"

GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

