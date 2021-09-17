inherit autotools-brokensep pkgconfig
require ../include/common-location-defines.inc

DESCRIPTION = "Synergy Loc API"
PR = "r1"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/synergy_loc_api;subpath=synergy_loc_api;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/qcom-opensource/location/synergy_loc_api"

DEPENDS = "loc-sll-if loc-core"

CPPFLAGS += "-I${WORKSPACE}/base/include"
PACKAGES = "${PN}"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
FILES_${PN} = "${libdir}/*"
FILES_${PN} += "/usr/include/"
FILES_${PN} += "/usr/lib/"
INSANE_SKIP_${PN} = "dev-so"
