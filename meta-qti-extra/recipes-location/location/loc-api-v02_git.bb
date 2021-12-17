require ../include/common-location-defines.inc
SUMMARY = "loc-api"
DESCRIPTION = "Loc API"
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "loc-core qmi-framework"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/loc_api/loc_api_v02;subpath=loc_api/loc_api_v02;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/qcom-opensource/location/loc_api/loc_api_v02"

inherit autotools-brokensep pkgconfig

