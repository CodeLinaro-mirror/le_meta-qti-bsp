DEPENDS += "gbm linux-msm-headers wayland-native weston"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-bad/gst-plugins-bad-${PV}.tar.xz"
SRC_URI_append = " ${PATH_TO_REPO}/gstreamer/gst-plugins-bad/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-bad;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-bad/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"
SRCREV_FORMAT = "bad_common"
S = "${WORKDIR}/gstreamer/gst-plugins-bad"

EXTRA_OECONF_append = " --with-protocal-xml-path=${STAGING_DATADIR}/weston"
EXTRA_OEMESON += "-Dyadif=disabled"

CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm"

do_compile_prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}
