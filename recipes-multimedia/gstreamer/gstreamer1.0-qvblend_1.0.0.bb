inherit autotools pkgconfig

SUMMARY = "Qvblend Plugin for GStreamer"
SECTION = "multimedia"
LICENSE = "LGPL-2.0 & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.0;md5=9427b8ccf5cf3df47c29110424c9641a \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}/gstreamer/gst-plugins-qti-oss:"
SRC_URI = "file://gst-plugin-qvblend"

LIBV = "1.0"
S      = "${WORKDIR}/gst-plugin-qvblend"

DEPENDS += "gstreamer1.0 \
            gstreamer1.0-plugins-bad \
            gstreamer1.0-plugins-base \
            "

FILES_${PN} += "${libdir}/gstreamer-${LIBV}/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-${LIBV}/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-${LIBV}/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-${LIBV}/*.a"

#Skips check for .so symlinks
INSANE_SKIP_${PN} = "dev-so"
INSANE_SKIP_${PN} += "installed-vs-shipped"
