DEFAULT_PREFERENCE = "-1"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad"
DEPENDS += "media"
RDEPENDS_${PN} = "media"
GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"
SRC_URI =  "${PATH_TO_REPO}/gstreamer/qti-gst-omx/.git;protocol=${PROTO};destsuffix=gstreamer/qti-gst-omx;usehead=1"
SRC_URI_append = " ${CLO_LE_GIT}/gstreamer/common;destsuffix=gstreamer/qti-gst-omx/common;branch=gstreamer/common/1.16;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "a825d2773adaeec23369d0770098b2c44ca7377a"
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
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CFLAGS_append = "-DVIDC_TARGET_USES_GKI"

delete_pkg_m4_file() {
    # Delete m4 files which we provide patched versions of but will be ignored
    # if these exist
	rm -f "${S}/common/m4/pkg.m4"
	rm -f "${S}/common/m4/gtk-doc.m4"
}

do_configure[prefuncs] += "delete_pkg_m4_file"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
