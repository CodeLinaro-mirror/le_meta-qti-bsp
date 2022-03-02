DEPENDS += "gbm linux-msm-headers wayland-native weston"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-bad/gst-plugins-bad-${PV}.tar.xz"
SRC_URI_append = " ${PATH_TO_REPO}/gstreamer/gst-plugins-bad/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-bad;usehead=1"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-bad"

EXTRA_OECONF_append = " --with-protocal-xml-path=${STAGING_DATADIR}/weston"
EXTRA_OEMESON += "-Dyadif=disabled"

CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm"

do_compile_prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}
