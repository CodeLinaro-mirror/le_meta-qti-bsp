DEFAULT_PREFERENCE = "-1"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-bad/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-bad;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-bad/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "bad_common"
S = "${WORKDIR}/gstreamer/gst-plugins-bad"

DEPENDS += "wayland-native"

EXTRA_OEMESON += " \
                  -Dyadif=disabled \
                 "
EXTRA_OEMESON_append = " \
			   -Dkernel_path=${STAGING_KERNEL_BUILDDIR}/usr/include \
              "
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_compile_prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}

