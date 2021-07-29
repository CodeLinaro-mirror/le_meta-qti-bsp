DESCRIPTION = "OMX video encoder lite sample"
SECTION = "mm-venc-omx-test-lite"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "glib-2.0 libcutils linux-msm-headers media"
SRCREV = "${AUTOREV}"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"

S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/omx-lite-app/mm-venc-omx-test"

inherit cmake

FILESPATH =+ "${WORKSPACE}:"

CFLAGS += "-include stdbool.h \
           -include stdint.h \
           -include signal.h \
           -include stdio.h"

CXXFLAGS += "${CFLAGS} \
             -I${STAGING_INCDIR}/glib-2.0 \
             -I${STAGING_INCDIR}/glib-2.0/include \
             -I${STAGING_INCDIR}/glib-2.0/glib \
             -I${STAGING_INCDIR}/c++ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
             -I${STAGING_INCDIR}/common/inc \
             -I${STAGING_INCDIR}/mm-osal/include \
             -I${STAGING_INCDIR}/mm-core/include \
             -I${STAGING_INCDIR}/mm-core \
             -I${STAGING_INCDIR}/linux-msm \
             -I${STAGING_INCDIR}/ion_headers"
