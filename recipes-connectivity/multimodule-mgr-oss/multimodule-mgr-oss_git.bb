inherit autotools-brokensep pkgconfig

DESCRIPTION = "Modem Manager Client Open Source"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r1"

DEPENDS += "glib-2.0 dbus"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://multimodule-mgr-oss"
SRC_DIR = "${WORKSPACE}/multimodule-mgr-oss"
S = "${WORKDIR}/multimodule-mgr-oss"

FILES_${PN} += "${libdir}/*.so"
PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev ${PN}-doc ${PN}-locale"
INSANE_SKIP_${PN} += " dev-so"

