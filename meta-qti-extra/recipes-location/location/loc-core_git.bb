require ../include/common-location-defines.inc
SUMMARY = "loc-core"
DESCRIPTION = "Loc Core"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "gps-utils"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps/core;subpath=core;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/hardware/qcom/gps/core"

inherit autotools-brokensep pkgconfig

