inherit qcommon 

DESCRIPTION = "Libunwind"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
DEPENDS = "libatomic-ops"

PR = "r0"
SRC_URI = "${CAF_LA_GIT}/platform/external/libunwind.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=external/libunwind"

S = "${WORKDIR}/external/libunwind"
