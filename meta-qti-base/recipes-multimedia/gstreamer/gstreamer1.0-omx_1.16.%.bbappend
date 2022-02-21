DEPENDS += "linux-msm-headers media"
DEPENDS_remove = "virtual/libomxil"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-omx/gst-omx-${PV}.tar.xz"
SRC_URI_append = " ${PATH_TO_REPO}/gstreamer/qti-gst-omx/.git;protocol=${PROTO};destsuffix=gstreamer/qti-gst-omx;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/qti-gst-omx/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"
SRCREV_FORMAT = "omx_common"

S = "${WORKDIR}/gstreamer/qti-gst-omx"

GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"

EXTRA_OEMESON = "\
                 -Dtarget=qti \
                 -Dheader_path=${STAGING_INCDIR}/mm-core \
                 -Dkernel_path=${STAGING_INCDIR}/linux-msm \
                 -Dstaging_inc_path=${STAGING_INCDIR} \
                 -Denable-target-vpu554=yes \
                "

CFLAGS_append = " -DVIDC_TARGET_USES_GKI"

RDEPENDS_${PN} = "media"
