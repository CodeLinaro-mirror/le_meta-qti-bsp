SUMMARY = "Multimedia libraries and SDK"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://hardware/qcom/media/"
S = "${WORKDIR}/hardware/qcom/media"

PR = "r1"
DEPENDS = "virtual/kernel"
DEPENDS += "glib-2.0"
DEPENDS += "virtual/libc"
DEPENDS += "libcutils liblog liblog-native system-core"
DEPENDS += "weston"
DEPENDS += "${@base_conditional('WITH_PROP_LAYER', 'no', 'display-hal-linux', 'mm-video-noship',d)}"

# Need the kernel headers
PACKAGE_ARCH = "${MACHINE_ARCH}"

LV = "1.0.0"

inherit autotools

#re-use non-perf settings
#BASEMACHINE = "${@d.getVar('MACHINE', True).replace('-perf', '')}"
BASEMACHINE = "msm8974"

#EXTRA_OECONF_append = "--with-libhardware-headers=${WORKSPACE}/hardware/libhardware "
#EXTRA_OECONF_append = "--with-sanitized-headers=${STAGING_KERNEL_DIR}/include/uapi "
#EXTRA_OECONF_append = "--with-common-includes=${STAGING_KERNEL_DIR}/include "
#EXTRA_OECONF_append = "${@base_conditional('BASEMACHINE', 'msm8655', ' --enable-target-msm7630=yes', '', d)} "
#EXTRA_OECONF_append = "${@base_conditional('BASEMACHINE', 'msm8960', ' --enable-target-msm8960=yes', '', d)} "
EXTRA_OECONF_append = "${@base_conditional('BASEMACHINE', 'msm8974', ' --enable-target-msm8974=yes', '', d)} "
EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96autogvmquin', ' --enable-target-hypervisor=yes', '', d)} "
EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96autogvmred', ' --enable-target-hypervisor=yes', '', d)} "
EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96autogvmgh', ' --enable-target-hypervisor=yes', '', d)} "
EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96auto', ' --enable-target-uses-gbm=yes', '', d)} "
EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96autodvrs', ' --enable-targets-that-support-pq=yes', '', d)} "
#EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96autodvrs', ' --enable-targets-that-support-adsp-pq=yes', '', d)} "
EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96autodvrs', ' --enable-target-uses-gbm=yes', '', d)} "

EXTRA_OECONF_append = "${@base_conditional('MACHINE', '8x96autogvmquin', ' --enable-target-uses-gbm=yes', '', d)} "

EXTRA_OECONF_append =" --enable-use-glib="yes""
EXTRA_OECONF_append =" --enable-target-uses-ion="yes""
EXTRA_OECONF_append =" --enable-target-${SOC_FAMILY}="yes""
EXTRA_OECONF_append =" --enable-target-uses-media-extensions="no""
EXTRA_OECONF_append_msm8996 =" --enable-build-mm-video="yes""
EXTRA_OECONF_append_msm8996 =" --enable-is-ubwc-supported="yes""
EXTRA_OECONF_append_msm8996 =" --enable-master-side-cp-target-list="yes""

python __anonymous () {
  # add early_init specified patch
  if bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', True, False, d):
      d.appendVar("SRC_URI", " file://0001-disable-port-setting-change-for-early-image.patch")
}

CPPFLAGS += "-I${STAGING_INCDIR} \
             -I${STAGING_INCDIR}/drm \
             -I${STAGING_INCDIR}/EGL \
             -I${STAGING_INCDIR}/GLES2 \
             -I${STAGING_INCDIR}/glib-2.0 \
             -I${STAGING_LIBDIR}/glib-2.0/include \
             -I${STAGING_LIBDIR}/glib-2.0/glib \
             -I${STAGING_INCDIR}/c++ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
             -I${STAGING_INCDIR}/libqdutils \
	     -I${STAGING_INCDIR}/qcom/display \
	     -I${STAGING_INCDIR}/libpqstats"
CPPFLAGS += "-include stdint.h"

LDFLAGS += "-lglib-2.0"
LDFLAGS += "-lgbm"
LDFLAGS += "-ldrm"
LDFLAGS += "-lwayland-client"
LDFLAGS += "-lEGL"

FILES_${PN}-dev = "\
    ${includedir}/* \
    ${libdir}/*.la"

FILES_${PN} = "\
    ${libdir}/* \
    ${bindir}/* \
    ${datadir}/*"

#Skips check for .so symlinks
INSANE_SKIP_${PN} = "dev-so"

do_install() {
	oe_runmake DESTDIR="${D}/" LIBVER="${LV}" install
	mkdir -p ${STAGING_INCDIR}/mm-core
	install -m 0644 ${S}/mm-core/inc/*.h ${STAGING_INCDIR}/mm-core
	mkdir -p ${STAGING_INCDIR}/libstagefrighthw
	install -m 0644 ${S}/libstagefrighthw/QComOMXMetadata.h ${STAGING_INCDIR}/libstagefrighthw
}
