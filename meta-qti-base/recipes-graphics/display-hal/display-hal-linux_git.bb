SUMMARY = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

DEPENDS += "system-core"
DEPENDS += "libhardware"
DEPENDS += "binder"
DEPENDS += "drm"
DEPENDS += "libdrm"
#DEPENDS += "adreno"
DEPENDS += "gbm-headers"
DEPENDS += "display-commonsys-intf-linux"

PR = "r8"

SRC_DIR = "${SRC_DIR_ROOT}/display/display-hal"
SRC_URI = "${PATH_TO_REPO}/display/display-hal/.git;protocol=${PROTO};destsuffix=display/display-hal;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/display/display-hal"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += " --enable-sdmhaldrm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DCOMPILE_DRM"
CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdrmutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqdutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqservice"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/sdm/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdebug"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"

# fix for uapi msm_drm.h header file related compilation issue
CPPFLAGS += "-fno-operator-names"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
do_install_append () {
    install -m 0644 ${WORKDIR}/display/display-hal/include/* ${STAGING_INCDIR}
    install -m 0664 ${WORKDIR}/display/display-hal/gpu_tonemapper/*.h ${STAGING_INCDIR}
}

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
