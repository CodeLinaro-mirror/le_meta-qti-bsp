SUMMARY = "Virtual fastrpc driver unit test"
DESCRIPTION = "This test application depend on googletest framework and compiled as executable binary, it can test on cdsp signed PD."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://../NOTICE;md5=13f203e044394131efb7effadef55476"

DEPENDS += "adsprpc glib-2.0 googletest"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/unit-test/applib-unit-test/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/unit-test/applib-unit-test/fastrpc_ut;subpath=fastrpc_ut;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/unit-test/applib-unit-test/fastrpc_ut"

inherit autotools-brokensep pkgconfig

CXXFLAGS += "--sysroot=${STAGING_DIR_TARGET} -I${STAGING_KERNEL_BUILDDIR}/usr/include -I${STAGING_INCDIR}/adsprpc/inc"

do_compile() {
        ${CXX} ${CXXFLAGS}  -D__linux__ -o fastrpc_ut fastrpc-ut.cpp -lglib-2.0 -lm -lgtest -lcdsprpc
}

do_install() {
        install -d ${D}${bindir}
        install -m 0755 ${S}/fastrpc_ut ${D}${bindir}
}

RDEPENDS:${PN} += "glib-2.0 googletest"
