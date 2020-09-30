DESCRIPTION = "Provide native media hardware Headers"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
SRCREV = "${AUTOREV}"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1"

S = "${WORKDIR}/frameworks"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install() {
    install -d ${D}${includedir}/media/hardware
    install -m 0644 ${S}/native/include/media/hardware/*.h -D ${D}${includedir}/media/hardware/
}

ALLOW_EMPTY_${PN} = "1"
