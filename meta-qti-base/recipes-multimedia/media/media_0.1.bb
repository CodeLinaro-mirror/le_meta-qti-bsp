SUMMARY = "Multimedia libraries and SDK"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS = "glib-2.0"
DEPENDS += "virtual/libc"
DEPENDS += "virtual/egl"
DEPENDS += "libion libcutils libutils system-core-headers"
DEPENDS += "mm-video-noship"
DEPENDS += "libdrm gbm wayland gbm-headers"
DEPENDS += "display-commonsys-intf-linux display-hal-headers"
DEPENDS += "media-plugin-headers"
SRCREV = "${AUTOREV}"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/media/.git;protocol=${PROTO};destsuffix=hardware/qcom/media;usehead=1"

S = "${WORKDIR}/hardware/qcom/media"

inherit autotools

EXTRA_OECONF_append = " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF_append = " --with-kernel-headers=${STAGING_KERNEL_BUILDDIR}/include"
EXTRA_OECONF_append = " --with-adreno-includes=${STAGING_INCDIR}/adreno"
EXTRA_OECONF_append = " --with-cutils-headers=${STAGING_INCDIR}/cutils/"
EXTRA_OECONF_append = " --with-log-headers=${STAGING_INCDIR}/log/"
EXTRA_OECONF_append = " --with-usr-include-headers=${STAGING_INCDIR}/"
EXTRA_OECONF_append = " --enable-use-glib='yes'"
EXTRA_OECONF_append = " --enable-target-uses-ion='yes'"
EXTRA_OECONF_append = " --enable-target-uses-gbm='yes'"
EXTRA_OECONF_append = " --enable-target-uses-media-extensions='no'"
EXTRA_OECONF_append = " --enable-build-mm-video='yes'"
EXTRA_OECONF_append = " --enable-is-ubwc-supported='yes'"
EXTRA_OECONF_append = " --enable-build-swcodec='yes'"
EXTRA_OECONF_append = " --enable-target-output-deinterlaced='yes'"
EXTRA_OECONF_append = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', ' --enable-target-hypervisor=yes', '', d)}"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
do_install_append() {
   install -d ${D}${includedir}/mm-core
   install -m 0644 ${S}/mm-core/inc/*.h -D ${D}${includedir}/mm-core/
   install -d ${D}${includedir}/venc/inc
   install -m 0644 ${S}/mm-video-v4l2/vidc/venc/inc/omx_video_common.h -D ${D}${includedir}/venc/inc/
   install -m 0644 ${S}/mm-video-v4l2/vidc/venc/inc/omx_video_base.h -D ${D}${includedir}/venc/inc/
   install -d ${D}${includedir}/vdec/inc
   install -m 0644 ${S}/mm-video-v4l2/vidc/vdec/inc/*.h -D ${D}${includedir}/vdec/inc/
   install -m 0644 ${S}/mm-video-v4l2/vidc/common/inc/*.h -D ${D}${includedir}/
   install -d ${D}${includedir}/libstagefrighthw
   install -m 0644 ${S}/libstagefrighthw/QComOMXMetadata.h -D ${D}${includedir}/libstagefrighthw/
   install -m 0644 ${S}/libc2dcolorconvert/C2DColorConverter.h ${D}${includedir}/
   if ${@bb.utils.contains('DISTRO_FEATURES', 'early_userspace', 'true', 'false', d)}; then
       install -d ${D}/usr/bin
       install -m 0777 ${THISDIR}/test_1080p.h264 ${D}/usr/bin/test_1080p.h264
   fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES_${PN} += "\
    ${datadir}/*"

CPPFLAGS += "-I${STAGING_INCDIR} \
             -I${STAGING_INCDIR}/drm \
             -I${STAGING_INCDIR}/EGL \
             -I${STAGING_INCDIR}/GLES2 \
             -I${STAGING_INCDIR}/glib-2.0 \
             -I${STAGING_LIBDIR}/glib-2.0/include \
             -I${STAGING_LIBDIR}/glib-2.0/glib \
             -I${STAGING_INCDIR}/c++ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
             -I${STAGING_INCDIR}/ion_headers  \
             -I${STAGING_INCDIR}/disp-commonsys-intf/display \
             -I${STAGING_INCDIR}/mm-video/swvdec \
             -I${STAGING_INCDIR}/mm-video/swvenc"
CPPFLAGS += "-include stdint.h"

LDFLAGS += "-lglib-2.0"
LDFLAGS += "-lgbm"
LDFLAGS += "-ldrm"
LDFLAGS += "-lwayland-client"
LDFLAGS += "-lEGL"
LDFLAGS += "-lqdMetaData"
