inherit autotools qcommon

DESCRIPTION = "WFA certification testing tool for QCA devices"
HOMEPAGE = "https:.github.com/qca/sigma-dut"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"

SRC_DIR = "${SRC_DIR_ROOT}/wlan/utils/sigma-dut/"
SRC_URI   = "git://source.codeaurora.org/platform/vendor/qcom-opensource/wlan/utils/sigma-dut.git;protocol=https;destsuffix=wlan/utils/sigma-dut;nobranch=1"
S = "${WORKDIR}/wlan/utils/sigma-dut"
SRCREV = "98c4b198b3010fa27f88d45d5bb744be0e3df852"

do_install() {
    make install DESTDIR=${D} BINDIR=${sbindir}/
}
