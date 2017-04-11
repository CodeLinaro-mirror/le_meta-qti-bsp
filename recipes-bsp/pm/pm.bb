DESCRIPTION = "power management setting"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI += "file://cpufreq-gov.sh"
SRC_URI +="file://cpufreq-gov.service"

inherit systemd
SYSTEMD_SERVICE_${PN} = "cpufreq-gov.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
	install -D -m 0755 ${WORKDIR}/cpufreq-gov.sh ${D}${bindir}/cpufreq-gov.sh
	install -D -m 0644 ${WORKDIR}/cpufreq-gov.service ${D}${systemd_unitdir}/system/cpufreq-gov.service
}

FILES_${PN} += "usr/bin/cpufreq-gov.sh\
		${systemd_unitdir}/system/cpufreq-gov.service"
