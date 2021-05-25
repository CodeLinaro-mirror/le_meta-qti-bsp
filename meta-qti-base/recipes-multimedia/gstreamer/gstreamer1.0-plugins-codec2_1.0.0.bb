SUMMARY = "QTI codec2 Plugin for GStreamer"
DESCRIPTION = "Gstreamer H/W decoder and encoder plugins based on codec2 APIs"
HOMEPAGE = "https://www.codeaurora.org/"
SECTION = "multimedia"
LICENSE = "LGPL-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.0;md5=9427b8ccf5cf3df47c29110424c9641a \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

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

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/gstreamer-1.0/*.so"

inherit autotools-brokensep

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

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

TOOLCHAIN = "clang"
