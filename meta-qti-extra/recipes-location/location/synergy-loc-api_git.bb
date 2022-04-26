require ../include/common-location-defines.inc
SUMMARY = "synergy-loc-api"
DESCRIPTION = "Synergy Loc API"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "loc-core loc-sll-if"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/synergy_loc_api;subpath=synergy_loc_api;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/qcom-opensource/location/synergy_loc_api"

inherit autotools-brokensep pkgconfig

PACKAGES = "${PN}"

FILES:${PN} += "\
    ${libdir}/* \
    ${includedir}/ \
"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} = "dev-so"
