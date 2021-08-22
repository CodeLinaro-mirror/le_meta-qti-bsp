SUMMARY = "Secure video playback demo for GStreamer"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS = "glib-2.0"
DEPENDS += "gstreamer1.0 \
            gstreamer1.0-plugins-base \
            linux-msm-headers \
            media \
            virtual/libc \
           "
SRCREV = "${AUTOREV}"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"

S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/secureappsrc"

inherit autotools-brokensep pkgconfig

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/glib-2.0 \
           -I${STAGING_INCDIR}/../lib/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/glib \
           -I${STAGING_INCDIR}/c++ \
           -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
           -I${STAGING_INCDIR}/gstreamer-1.0 \
           -I${STAGING_INCDIR}/linux-msm \
           -I${STAGING_INCDIR}/ion_headers \
           -I${STAGING_INCDIR}/mm-core/"
