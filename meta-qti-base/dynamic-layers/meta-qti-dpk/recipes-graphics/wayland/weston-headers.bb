SUMMARY = "Install extra headers for the Weston Wayland compositor"
DESCRIPTION = "Provide weston header file for dpk image"
LICENSE = "MIT"
HOMEPAGE = "https://git.codelinaro.org/"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "${PATH_TO_REPO}/graphics/weston/.git;protocol=${PROTO};destsuffix=graphics/weston;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/graphics/weston"

PREBUILT = "1"

do_configure[noexec] = "1"

do_compile[noexec] = "1"

do_install(){
    install -d ${D}${includedir}/weston-shared
    install -m 0644 ${S}/shared/string-helpers.h ${D}${includedir}/weston-shared/
}

ALLOW_EMPTY:${PN} = "1"
