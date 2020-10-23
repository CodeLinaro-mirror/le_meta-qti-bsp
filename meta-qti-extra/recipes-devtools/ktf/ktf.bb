DESCRIPTION = "Kernel Test Framework(KTF) implements an unit test framework for the Linux Kernel"
HOMEPAGE = "https://github.com/oracle/ktf/"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

KTF_MODULE_NAME = "ktf"

SRC_URI = "\
    git://source.codeaurora.org/quic/le/external/oracle/ktf;protocol=http;branch=circumflex/master \
"
SRCREV = "2a575dcd6173ae893e88a618f4e890d8658bd25a"
S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig module module-sign

DEPENDS += "gtest libnl"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

EXTRA_OECONF = "KDIR=${STAGING_KERNEL_DIR}"

SECURITY_CFLAGS = "${SECURITY_NO_PIE_CFLAGS}"
CPPFLAGS += "-I${STAGING_INCDIR}"

MODULES_MODULE_SYMVERS_LOCATION = "kernel"
MODULES_INSTALL_TARGET = ""

do_install_append () {
    install -d ${D}${bindir}
    install -d ${D}${libdir}
    install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/unit_test
    install -m 0644 ${S}/kernel/${KTF_MODULE_NAME}.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/unit_test/
    install -m 0755 ${S}/user/.libs/ktfrun ${D}${bindir}
    install -m 0755 ${S}/lib/.libs/libktf.so.0 ${D}${libdir}
    install -m 0644 ${S}/kernel/*.h ${STAGING_KERNEL_DIR}/include/linux
}

FILES_${PN} += "${bindir} ${libdir} ${base_libdir}/modules/${KERNEL_VERSION}/unit_test/${KTF_MODULE_NAME}.ko"
FILES_${PN}-dbg += "${bindir}/.debug/ktfrun ${libdir}/.debug"
FILES_SOLIBSDEV = ""
