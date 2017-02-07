inherit autotools-brokensep pkgconfig

DESCRIPTION = "Libunwind"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
DEPENDS = "libatomic-ops"

PR = "r0"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://external/libunwind/"
S = "${WORKDIR}/external/libunwind"

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto', 'libunwind_auto', 'none',d)}"
include ${INCSUFFIX}.inc
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autofusion', 'libunwind_auto', 'none',d)}"
include ${INCSUFFIX}.inc
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto44', 'libunwind_auto', 'none',d)}"
include ${INCSUFFIX}.inc