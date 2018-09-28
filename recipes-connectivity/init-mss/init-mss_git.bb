inherit autotools-brokensep systemd

DESCRIPTION = "Modem init"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
PR = "r7"

FILESPATH =+ "${WORKSPACE}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/init_mss:"

SRC_URI = "file://mdm-ss-mgr/init_mss/"
SRC_URI += "file://init_sys_mss.service"

DEPENDS += "systemd"

S = "${WORKDIR}/mdm-ss-mgr/init_mss/"
EXTRA_OECONF += " ${@base_contains('BASEMACHINE', 'apq8009', '--enable-indefinite-sleep', '', d)}"
EXTRA_OECONF += " ${@base_contains('BASEMACHINE', 'apq8017', '--enable-indefinite-sleep', '', d)}"
EXTRA_OECONF += " ${@base_contains('BASEMACHINE', 'apq8053', '--enable-indefinite-sleep', '', d)}"
EXTRA_OECONF += " ${@base_contains('BASEMACHINE', 'apq8096', '--enable-indefinite-sleep', '', d)}"

CFLAGS += "-DSLEEP_INDEFINITE"
CFLAGS += "-lsystemd"

SYSTEMD_SERVICE_${PN} = "init_sys_mss.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
    install -m 0755 ${S}/init_mss -D ${D}/sbin/init_mss
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/init_sys_mss.service -D ${D}${systemd_unitdir}/system/init_sys_mss.service

}

FILES_${PN} += "${systemd_unitdir}/system/"

