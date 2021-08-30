inherit autotools-brokensep pkgconfig
require ../include/common-location-defines.inc

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DESCRIPTION = "location integration api library"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/integration_api;subpath=integration_api;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/qcom-opensource/location/integration_api"

DEPENDS = "glib-2.0 loc-pla-hdr libcutils gps-utils location-api-msg-proto"

SOLIBS=".so"
FILES_SOLIBSDEV=""
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
CPPFLAGS += "-I${WORKSPACE}/base/include"
PACKAGES = "${PN}"
FILES_${PN} = "${libdir}/*"
FILES_${PN} += "/usr/include/"
FILES_${PN} += "/usr/lib/"
INSANE_SKIP_${PN} = "dev-so"
