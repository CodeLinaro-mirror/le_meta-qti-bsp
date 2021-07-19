SUMMARY = "videoblend plugins for GStreamer"
DESCRIPTION = "build the videoblend plugins for GStreamer, blend the rendered text buffer on top of video stream"
HOMEPAGE = "https://www.codeaurora.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause & LGPL-2.0+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                   "

DEPENDS += "glib-2.0 \
            gstreamer1.0 \
            gstreamer1.0-plugins-base \
            media \
            virtual/libc \
           "

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-qvblend"

inherit autotools-brokensep pkgconfig

CPPFLAGS += "-I${STAGING_INCDIR}/glib-2.0 \
             -I${STAGING_LIBDIR}/glib-2.0/include \
             -I${STAGING_INCDIR}/c++ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
             -I${STAGING_KERNEL_BUILDDIR}/usr/include \
             -I${STAGING_INCDIR}/mm-core \
             -DUSE_GBM \
            "

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILES_${PN} += "${libdir}/gstreamer-1.0/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-1.0/*.a"
