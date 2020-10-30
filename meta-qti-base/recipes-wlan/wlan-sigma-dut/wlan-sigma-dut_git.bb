DESCRIPTION = "WFA certification testing tool for QCA devices"
HOMEPAGE = "https://github.com/qca/sigma-dut"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://README;md5=a5044fc88d4aecbffe1b1ad56ce8df9f"
SRCREV = "${AUTOREV}"
PR = "r0"

SRC_URI = "${PATH_TO_REPO}/wlan/utils/sigma-dut/.git;protocol=${PROTO};destsuffix=wlan/utils/sigma-dut;usehead=1"

SRC_DIR = "${SRC_DIR_ROOT}/wlan/utils/sigma-dut/"
S = "${WORKDIR}/wlan/utils/sigma-dut"

inherit autotools-brokensep pkgconfig

do_install() {
    make install DESTDIR=${D} BINDIR=${sbindir}/
}

PACKAGE_ARCH ?= "${MACHINE_ARCH}"
