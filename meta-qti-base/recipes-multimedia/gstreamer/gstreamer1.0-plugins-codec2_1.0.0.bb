SUMMARY = "QTI codec2 Plugin for GStreamer"
DESCRIPTION = "Gstreamer H/W decoder and encoder plugins based on codec2 APIs"
HOMEPAGE = "https://www.codeaurora.org/"
SECTION = "multimedia"
LICENSE = "LGPL-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://NOTICE;md5=e45e8fc7bdec198bec59831b0d7d16f7"

DEPENDS += "codec2 \
            glib-2.0 \
            gstreamer1.0 \
            gstreamer1.0-plugins-bad \
            gstreamer1.0-plugins-base \
            media-codec2 \
            media-external"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"

S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-codec2"

inherit autotools-brokensep

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/../lib64/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0 \
           -I${STAGING_INCDIR}/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/glib \
           -I${STAGING_INCDIR}/c++ \
           -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
           -I${STAGING_KERNEL_BUILDDIR}/usr/include/vidc \
           -I${STAGING_KERNEL_BUILDDIR}/usr/include"

CXXFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include/vidc"
CXXFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES_${PN} += "${libdir}/gstreamer-1.0/*.so"

TOOLCHAIN = "clang"
