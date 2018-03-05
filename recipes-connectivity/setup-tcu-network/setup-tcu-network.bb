SUMMARY = "Scripts for setup TCU AGL GVM Network"
SECTION = "network"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"


inherit systemd

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINE', '8x96autogvmquintcu', 'setup-tcu-network', 'none',d)}"
#FOLDERSUFFIX = "${@base_conditional('MACHINE', '8x96autogvmquintcu', 'setup-tcu-network-8x96autogvmquintcu', 'setup-network-conf',d)}"

FILESEXTRAPATHS_append := ":${THISDIR}/setup-tcu-network"
SRC_URI = " file://setup-network.sh \
            file://setup-network.service \
          "

do_install() {
}

# Including the file depends on chipset
include ${INCSUFFIX}-${MACHINE}.inc


