DEPENDS += "gbm linux-msm-headers wayland-native weston"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/waylandsink:"
SRC_URI:append = " \
    file://0001-gstwaylandsink-add-P010_10LE-support-statement.patch \
    file://0002-waylandsink-support-ubwc-modifier-and-zwp_linux_dmab.patch \
"

EXTRA_OECONF:append = " --with-protocal-xml-path=${STAGING_DATADIR}/weston"

CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm"

do_compile:prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}

PACKAGECONFIG:remove = "vulkan"
