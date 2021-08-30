inherit autotools-brokensep pkgconfig
require ../include/common-location-defines.inc

DESCRIPTION = "location api interface"
PR = "r1"

DEPENDS = "gps-utils"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps/location;subpath=location;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/hardware/qcom/gps/location"
