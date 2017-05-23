inherit autotools-brokensep pkgconfig

DESCRIPTION = "Modem Manager Client Open Source"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r1"

DEPENDS += "glib-2.0 dbus"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://multimodule-mgr-oss"

SRC_DIR = "${WORKSPACE}/multimodule-mgr-oss"

S = "${WORKDIR}/multimodule-mgr-oss"

FILES_${PN} += "${libdir}/*.so"
FILES_${PN} += "/data/misc/mmclient"
PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev ${PN}-doc ${PN}-locale"
INSANE_SKIP_${PN} += " dev-so"

do_install_append() {
    install -m 644 ${WORKDIR}/multimodule-mgr-oss/mdmmgr_client/src/mdmmgr_client.conf -D ${D}${sysconfdir}/dbus-1/system.d/mdmmgr_client.conf
    install -d ${D}/data/misc/mmclient
}

