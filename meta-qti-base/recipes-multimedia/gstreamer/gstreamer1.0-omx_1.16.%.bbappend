DEPENDS += "linux-msm-headers media"
DEPENDS:remove = "virtual/libomxil"

SRC_URI:remove = "https://gstreamer.freedesktop.org/src/gst-omx/gst-omx-${PV}.tar.xz"
SRC_URI:append = " ${PATH_TO_REPO}/gstreamer/qti-gst-omx/.git;protocol=${PROTO};destsuffix=gstreamer/qti-gst-omx;usehead=1"

SRCREV = "${AUTOREV}"

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
EXTRA_OEMESON:append:sa6155 = " -Denable-target-vpu554-video-6155=yes"

CFLAGS:append = " -DVIDC_TARGET_USES_GKI"

RDEPENDS:${PN} = "media"
