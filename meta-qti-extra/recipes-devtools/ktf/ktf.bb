DESCRIPTION = "Kernel Test Framework(KTF) implements a unit test framework for the Linux Kernel"
HOMEPAGE = "https://github.com/oracle/ktf/"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

KTF_MODULE_NAME = "ktf"

SRC_URI = "\
    ${CLO_LE_GIT}/external/oracle/ktf;protocol=${CLO_PROTOCOL};nobranch=1;name=ktf \
    file://0001-Remove-Wno-packed-bitfield-compat-option-in-Makefile.patch \
"

SRCREV_ktf = "25c855b0c5c2f3903ce915ece1586f3a87c710f4"
S = "${WORKDIR}/git"
MODULES_PATH = "${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/unit_test/"

inherit autotools-brokensep pkgconfig module module-sign


DEPENDS += "virtual/kernel gtest libnl"

EXTRA_OECONF = "KDIR=${STAGING_KERNEL_DIR}"

SECURITY_CFLAGS = "${SECURITY_NO_PIE_CFLAGS}"
CPPFLAGS += "-I${STAGING_INCDIR}"

MODULES_MODULE_SYMVERS_LOCATION = "kernel"
MODULES_INSTALL_TARGET = ""

do_install_append() {
    install -d ${D}${bindir}
    install -d ${D}${libdir}
    install -d ${D}/lib/modules/${KERNEL_VERSION}/unit_test
    install -D -m 0644 ${S}/kernel/${KTF_MODULE_NAME}.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
    install -m 0755 ${S}/user/.libs/ktfrun ${D}${bindir}
    cp ${S}/lib/.libs/libktf.so.0 ${D}${libdir}
    install -m 0644 ${S}/kernel/*.h ${STAGING_KERNEL_DIR}/include/linux
}

PCKAGES = "${PN} ${PN}-dbg"
FILES_${PN} = "${bindir}/ktfrun"
FILES_${PN}-dbg = "${bindir}/.debug/ktfrun"
FILES_${PN} += "${libdir}/lib*.so.0"
FILES_${PN}-dbg += "${libdir}/.debug"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/${KTF_MODULE_NAME}.ko"
FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} = "dev-so"
