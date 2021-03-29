DEFAULT_PREFERENCE = "-1"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad"
DEPENDS += "media"
RDEPENDS_${PN} = "media"
GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"
SRC_URI =  "${PATH_TO_REPO}/gstreamer/qti-gst-omx/.git;protocol=${PROTO};destsuffix=gstreamer/qti-gst-omx;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/qti-gst-omx/common;branch=gstreamer/common/master;name=common"
SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "omx_common"
S = "${WORKDIR}/gstreamer/qti-gst-omx"
EXTRA_OEMESON = " \
               -Dtarget=qti \
               -Dheader_path=${STAGING_INCDIR}/mm-core \
		-Dkernel_path=${STAGING_KERNEL_BUILDDIR}/usr/include \
		-Dstaging_inc_path=${STAGING_INCDIR} \
              "
EXTRA_OEMESON_append =" -Denable-target-vpu554=yes"
EXTRA_OEMESON_append =" -Denable-encoder-heic=yes"

CFLAGS_append = "-DVIDC_TARGET_USES_GKI"
