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
#DEPENDS += "adreno"
#RDEPENDS_{PN} = "mm-video-prop"
#INSANE_SKIP = 1

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

CPPFLAGS += "-I${STAGING_INCDIR} \
             -I${STAGING_INCDIR}/glib-2.0 \
             -I${STAGING_LIBDIR}/glib-2.0/include \
             -I${STAGING_INCDIR}/c++ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS}"
CPPFLAGS += "-include stdint.h"

LDFLAGS += "-lglib-2.0"

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
}
