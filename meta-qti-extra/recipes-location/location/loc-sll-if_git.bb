inherit autotools-brokensep pkgconfig
require ../include/common-location-defines.inc

DESCRIPTION = "Loc API Interface"
PR = "r1"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/loc_sll_if/if;subpath=loc_sll_if/if;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/qcom-opensource/location/loc_sll_if/if"

PACKAGES = "${PN}"
FILES_${PN} += "/usr/*"
FILES_${PN} += "/usr/include/*"

# Skip the unwanted steps
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/*.h ${D}${includedir}
}
