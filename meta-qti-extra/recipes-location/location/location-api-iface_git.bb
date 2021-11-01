require ../include/common-location-defines.inc
SUMMARY = "location-api-iface"
DESCRIPTION = "location api interface"
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps/location;subpath=location;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/hardware/qcom/gps/location"

# Skip the unwanted steps
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/*.h ${D}${includedir}
}
