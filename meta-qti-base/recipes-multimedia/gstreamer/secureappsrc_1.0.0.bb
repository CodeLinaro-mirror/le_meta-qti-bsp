inherit qcommon

SUMMARY = "Secure video playback demo for GStreamer"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI   =  "git://source.codeaurora.org/platform/vendor/qcom-opensource/gst-plugins-qti-oss.git;protocol=https;destsuffix=gstreamer/gst-plugins-qti-oss;nobranch=1"
SRC_DIR = "${SRC_DIR_ROOT}/gstreamer/gst-plugins-qti-oss/secureappsrc"
S      = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/secureappsrc"
PR = "r1"
LV = "1.0.0"
LIBV = "1.0"
SRCREV="9135db2be8079a1d96691fe3a9e853ce0f94285b"

DEPENDS = "glib-2.0"
DEPENDS += "media"
DEPENDS += "gstreamer1.0 \
            gstreamer1.0-plugins-base \
           "
DEPENDS += "virtual/libc"

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/glib-2.0 \
           -I${STAGING_INCDIR}/../lib/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/glib \
           -I${STAGING_INCDIR}/c++ \
           -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
           -I${STAGING_INCDIR}/gstreamer-1.0"
CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CFLAGS += "-I${STAGING_INCDIR}/ion_headers"
CFLAGS += "-I${STAGING_INCDIR}/mm-core/"

FILES_${PN}-dbg += "${bindir}/.debug"

#Skips check for .so symlinks
INSANE_SKIP_${PN} = "dev-so"


