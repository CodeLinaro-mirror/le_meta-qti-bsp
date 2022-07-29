SUMMARY = "location-client-api-hdr"
DESCRIPTION = "Location Client Api Hdr"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/client_api/inc/;subpath=client_api/inc;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/qcom-opensource/location/client_api/inc"

# Skip the unwanted steps
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/*.h ${D}${includedir}
}
