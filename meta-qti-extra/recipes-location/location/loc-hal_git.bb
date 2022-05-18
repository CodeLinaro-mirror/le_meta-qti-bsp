require ../include/common-location-defines.inc
SUMMARY = "loc-hal"
DESCRIPTION = "GPS Loc HAL"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "loc-core"

PR = "r5"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps;subpath=gps;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/hardware/qcom/gps"

inherit autotools-brokensep pkgconfig

do_install:append() {
    #Install default gps.conf file
    install -m 0644 -D ${S}/etc/gps.conf ${D}${sysconfdir}/gps.conf
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
