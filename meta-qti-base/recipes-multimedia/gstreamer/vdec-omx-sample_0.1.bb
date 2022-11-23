SUMMARY = "OMX video decoder test sample"
DESCRIPTION = "An OMX sample for video decoder, test H263, H264, H265, MPEG4, VP8, VP9, VC1, UBWC and TP10 decoding, flip, rotate and related features."
HOMEPAGE = "https://git.codelinaro.org"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "\
    linux-msm-headers \
    media \
"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/omx-sample-app/vdec-omx-sample"

inherit cmake

CXXFLAGS += "\
    -I${STAGING_INCDIR}/common/inc \
    -I${STAGING_INCDIR}/mm-osal/include \
    -I${STAGING_INCDIR}/mm-core/include \
    -I${STAGING_INCDIR}/mm-core \
    -I${STAGING_INCDIR}/linux-msm \
"

