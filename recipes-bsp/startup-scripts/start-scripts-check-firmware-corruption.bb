DESCRIPTION = "check for firmware corruption"
HOMEPAGE = "http://codeaurora.org"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
LICENSE = "BSD"

SRC_URI +="file://${BASEMACHINE}/check_for_firmware_corruption.sh"
SRC_URI +="file://check_for_firmware_corruption.service"

inherit systemd

SYSTEMD_SERVICE_${PN} = " check_for_firmware_corruption.service "
SYSTEMD_AUTO_ENABLE_${pn} = "enable"

do_install() {
    install -m 0755 ${WORKDIR}/${BASEMACHINE}/check_for_firmware_corruption.sh -D ${D}${sysconfdir}/init.d/check_for_firmware_corruption.sh
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0755 ${WORKDIR}/${BASEMACHINE}/check_for_firmware_corruption.sh -D ${D}${sysconfdir}/initscripts/check_for_firmware_corruption.sh
        install -m 0644 ${WORKDIR}/check_for_firmware_corruption.service -D ${D}${systemd_unitdir}/system/check_for_firmware_corruption.service
    fi
}
