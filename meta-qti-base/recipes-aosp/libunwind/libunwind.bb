inherit autotools-brokensep pkgconfig

DESCRIPTION = "Libunwind"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
DEPENDS = "libatomic-ops"

PR = "r0"
SRC_URI   =  "git://source.codeaurora.org/platform/external/libunwind.git;protocol=https;destsuffix=external/libunwind;nobranch=1"
S = "${WORKDIR}/external/libunwind"

SRCREV = "7ae792b4d98fb654b494675ba0f541bf2e664d55"
