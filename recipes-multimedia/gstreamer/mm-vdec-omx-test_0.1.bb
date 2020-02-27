inherit qcommon cmake
DESCRIPTION = "OMX video decoder sample"
SECTION  = "mm-vdec-omx-test"

FILESPATH  =+ "${WORKSPACE}:"
SRC_URI    = "file://gstreamer/gst-sample/mm-vdec-omx-test"
SRC_DIR = "${WORKSPACE}/gstreamer/gst-sample/mm-vdec-omx-test"

S = "${WORKDIR}/gstreamer/gst-sample/mm-vdec-omx-test"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "glib-2.0"
DEPENDS += "libcutils"
DEPENDS += "weston"
DEPENDS += "media"
DEPENDS += "virtual/kernel"

CFLAGS += "-include stdbool.h"
CFLAGS += "-include stdint.h"
CFLAGS += "-include signal.h"
CFLAGS += "-include stdio.h"
CXXFLAGS += "${CFLAGS}"
CXXFLAGS += "-I${STAGING_INCDIR}/drm"
CXXFLAGS += "-I${STAGING_INCDIR}/EGL"
CXXFLAGS += "-I${STAGING_INCDIR}/GLES2"
CXXFLAGS += "-I${STAGING_INCDIR}/../lib64/glib-2.0/include"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0/include"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0/glib"
CXXFLAGS += "-I${STAGING_INCDIR}/c++"
CXXFLAGS += "-I${STAGING_INCDIR}/c++/${TARGET_SYS}"
CXXFLAGS += "-I${STAGING_INCDIR}/common/inc"
CXXFLAGS += "-I${STAGING_INCDIR}/mm-osal/include"
CXXFLAGS += "-I${STAGING_INCDIR}/mm-core/include"
CXXFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

FILES_${PN} += " \
   ${libdir}/* \
   ${bindir}/* \
   ${includedir}/* \
"

#PACKAGES = "${PN}"

#INSANE_SKIP_${PN} += "installed-vs-shipped"

INHIBIT_PACKAGE_STRIP="1"
INHIBIT_PACKAGE_DEBUG_SPLIT="1"
