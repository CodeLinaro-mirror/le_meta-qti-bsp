DESCRIPTION = "OMX video decoder lite sample"
SECTION = "mm-vdec-omx-test-lite"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "glib-2.0 libion libcutils linux-msm-headers media weston"
SRCREV = "${AUTOREV}"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"

SRC_DIR = "${SRC_DIR_ROOT}/gstreamer/gst-plugins-qti-oss/omx-lite-app/mm-vdec-omx-test"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/omx-lite-app/mm-vdec-omx-test"

inherit cmake

CFLAGS += "-include stdbool.h \
           -include stdint.h \
           -include signal.h \
           -include stdio.h"

CXXFLAGS += "${CFLAGS} \
             -I${STAGING_INCDIR} \
             -I${STAGING_INCDIR}/drm \
             -I${STAGING_INCDIR}/EGL \
             -I${STAGING_INCDIR}/GLES2 \
             -I${STAGING_INCDIR}/glib-2.0 \
             -I${STAGING_LIBDIR}/glib-2.0/include \
             -I${STAGING_LIBDIR}/glib-2.0/glib \
             -I${STAGING_INCDIR}/c++ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
             -I${STAGING_INCDIR}/common/inc \
             -I${STAGING_INCDIR}/mm-core \
             -I${STAGING_INCDIR}/linux-msm \
             -I${STAGING_INCDIR}/disp-commonsys-intf/display \
             -I${STAGING_INCDIR}/ion_headers"
