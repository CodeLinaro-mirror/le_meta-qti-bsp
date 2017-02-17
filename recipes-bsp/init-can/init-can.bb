DESCRIPTION = "Init can scripts"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"

SRC_URI += "file://init.qti.can.sh"
SRC_URI += "file://init_qti_can.service"

inherit systemd
SYSTEMD_SERVICE_${PN} = "init_qti_can.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
	install -D -m 0755 ${WORKDIR}/init.qti.can.sh ${D}${bindir}/init.qti.can.sh
	install -D -m 0644 ${WORKDIR}/init_qti_can.service ${D}${systemd_unitdir}/system/init_qti_can.service
}

FILES_${PN} += "usr/bin/init.qti.can.sh\
		lib/systemd/system/init_qti_can.service"
