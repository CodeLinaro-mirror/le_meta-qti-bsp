SUMMARY = "display commonsys intf Library"
DESCRIPTION = "Provide common display header files and libraries for \
other modules to use."
HOMEPAGE = "https://www.codeaurora.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "libcutils libhardware-headers liblog libutils"

PR = "r3"

SRC_DIR = "${SRC_DIR_ROOT}/vendor/qcom/opensource/commonsys-intf/display"
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/commonsys-intf/display/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/commonsys-intf/display;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

LDFLAGS += "-llog -lutils -lcutils"

CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/gralloc"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/libqdmetadata"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/include"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
do_install_append() {
    install -d ${D}${includedir}
    install -m 644 ${S}/gralloc/*.h ${D}${includedir}
    install -m 644 ${S}/include/*.h ${D}${includedir}
}

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
