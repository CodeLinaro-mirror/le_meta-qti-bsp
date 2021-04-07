SUMMARY = "QTI Screen shot SRC Plugin for GStreamer"
SECTION = "multimedia"
LICENSE = "LGPL-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://NOTICE.txt;md5=75cfdd159cf288058ddf776b13ec384c"
DEPENDS = "glib-2.0 wayland-native"
DEPENDS += "gstreamer1.0 \
            gstreamer1.0-plugins-base \
            gstreamer1.0-plugins-bad \
            weston \
           "
DEPENDS += "virtual/libc"
SRCREV = "${AUTOREV}"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"

SRC_DIR = "${SRC_DIR_ROOT}/gstreamer/gst-plugins-qti-oss/gst-plugin-qscreencapsrc"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-qscreencapsrc"

inherit autotools-brokensep pkgconfig

FILES_${PN} += "${libdir}/gstreamer-${LIBV}/*.so"

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/drm \
           -I${STAGING_INCDIR}/weston \
           -I${STAGING_INCDIR}/pixman-1 \
           -I${STAGING_INCDIR}/../lib64/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0 \
           -I${STAGING_INCDIR}/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/glib \
           -I${STAGING_INCDIR}/c++ \
           -I${STAGING_INCDIR}/c++/${TARGET_SYS}"
CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CFLAGS += "-DUSE_V6"

LIBV = "1.0"
