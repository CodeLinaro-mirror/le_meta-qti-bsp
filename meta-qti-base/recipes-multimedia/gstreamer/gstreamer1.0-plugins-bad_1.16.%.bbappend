DEPENDS += "gbm virtual/kernel-headers wayland-native weston"

SRC_URI:remove = "https://gstreamer.freedesktop.org/src/gst-plugins-bad/gst-plugins-bad-${PV}.tar.xz"
SRC_URI:append = " ${PATH_TO_REPO}/gstreamer/gst-plugins-bad/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-bad;usehead=1"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-bad"

EXTRA_OECONF:append = " --with-protocal-xml-path=${STAGING_DATADIR}/weston"
EXTRA_OEMESON += "-Dyadif=disabled"

CPPFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

do_compile:prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}
