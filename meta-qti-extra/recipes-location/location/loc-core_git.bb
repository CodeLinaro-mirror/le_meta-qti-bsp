inherit autotools-brokensep pkgconfig
require ../include/common-location-defines.inc

DESCRIPTION = "Loc Core"
PR = "r1"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps/core;subpath=core;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/hardware/qcom/gps/core"

DEPENDS += "gps-utils"

EXTRA_OECONF += "${@oe.utils.conditional('DISTRO', 'auto', '--with-auto_feature', '', d)}"
