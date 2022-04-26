require ../include/common-location-defines.inc
SUMMARY = "location-client-api-testapp"
DESCRIPTION = "location client api test application "
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "gps-utils location-client-api location-hal-daemon location-integration-api"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/client_api_testapp;subpath=client_api_testapp;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/qcom-opensource/location/client_api_testapp"

inherit autotools-brokensep pkgconfig

