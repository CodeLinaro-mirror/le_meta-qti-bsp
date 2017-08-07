inherit autotools-brokensep pkgconfig

DESCRIPTION = "GPS Loc Net Iface"
PR = "r5"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://qcom-opensource/location/loc_net_iface/"
S = "${WORKDIR}/qcom-opensource/location/loc_net_iface"
DEPENDS = "glib-2.0 gps-utils qmi qmi-framework loc-pla data data-items loc-hal"
EXTRA_OECONF = "--with-glib"

PACKAGES = "${PN}"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
FILES_${PN} = "${libdir}/*"
FILES_${PN} = "${libdir}/* ${sysconfdir}"
FILES_${PN} += "/usr/include/*"
FILES_${PN} += "/usr/include/${PN}/*"
INSANE_SKIP = "1"
INSANE_SKIP_${PN} = "dev-so"

