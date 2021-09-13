SUMMARY = "Secure video playback demo for GStreamer"
DESCRIPTION = "Secure video playback demo for GStreamer, supporting secure input buffer sharing."
HOMEPAGE = "https://www.codeaurora.org"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/gstreamer/gst-plugins-qti-oss/secureappsrc/README;md5=cbe307edd65af059d16f9f3980402ee8"

DEPENDS += "\
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    linux-msm-headers \
    media \
    virtual/libc \
"

PR = "r1"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/secureappsrc"

inherit autotools-brokensep pkgconfig

CFLAGS += "\
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_INCDIR}/../lib/glib-2.0/include \
    -I${STAGING_INCDIR}/glib-2.0/include \
    -I${STAGING_INCDIR}/glib-2.0/glib \
    -I${STAGING_INCDIR}/c++ \
    -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
    -I${STAGING_INCDIR}/gstreamer-1.0 \
    -I${STAGING_INCDIR}/linux-msm \
    -I${STAGING_INCDIR}/ion_headers \
    -I${STAGING_INCDIR}/mm-core/ \
"
