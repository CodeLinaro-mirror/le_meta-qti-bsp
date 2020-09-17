inherit autotools qcommon

DESCRIPTION = "Ntrip client integration api hdr"
PR = "r1"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location.git;protocol=${PROTO};destsuffix=qcom-opensource/location/ntrip-client-integration-api/;subpath=ntrip-client-integration-api;nobranch=1"
SRCREV = "4343c891132591ed9bf65fca3e35cf69658d5a63"
S = "${WORKDIR}/qcom-opensource/location/ntrip-client-integration-api"

FILES_${PN} += "/usr/*"
PACKAGES = "${PN}"

do_configure() {
}

do_compile() {
}

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/*.h ${D}${includedir}
}
