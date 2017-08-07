inherit autotools-brokensep pkgconfig

DESCRIPTION = "location api inerface"
PR = "r1"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://hardware/qcom/gps/location/"
S = "${WORKDIR}/hardware/qcom/gps/location"

PACKAGES = "${PN}"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
FILES_${PN} = "${libdir}/* ${sysconfdir}"
FILES_${PN} += "/usr/include/*"
FILES_${PN} += "/usr/include/location-api-iface/*"

do_configure() {
}

do_compile() {
}

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/*.h ${D}${includedir}
}
