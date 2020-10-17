DESCRIPTION = "WFA certification testing tool for QCA devices"
HOMEPAGE = "https://github.com/qca/sigma-dut"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
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
