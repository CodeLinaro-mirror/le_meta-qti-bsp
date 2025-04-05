inherit autotools qprebuilt pkgconfig

DESCRIPTION = "Service to collect Logs at shutdown "
LICENSE          = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"

PR = "r1"

DEPENDS = "zlib"
RDEPENDS_${PN} = "qru-mplane-app"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/QPlatformUtils:"
SRC_URI += "file://${BPN}"

S = "${WORKDIR}/${BPN}"


do_install_append() {
	install -d ${D}/${sysconfdir}
	install -d ${D}/${systemd_unitdir}/system
	install -d ${D}/${systemd_unitdir}/system/reboot.target.wants/
	install -d ${D}/${systemd_unitdir}/system/poweroff.target.wants/
	install -d ${D}/${systemd_unitdir}/system/halt.target.wants/
	install -m 0644 ${S}/collect_shutdown_logs.service ${D}/${systemd_unitdir}/system/collect_shutdown_logs.service
	install -m 0644 ${S}/collect_default_logs.service ${D}/${systemd_unitdir}/system/collect_default_logs.service
	install -m 0644 ${S}/shutdown-logs.conf -D ${D}${sysconfdir}/shutdown-logs.conf
	ln -sf ${systemd_unitdir}/system/collect_shutdown_logs.service ${D}${systemd_unitdir}/system/reboot.target.wants/collect_shutdown_logs.service
	ln -sf ${systemd_unitdir}/system/collect_shutdown_logs.service ${D}${systemd_unitdir}/system/poweroff.target.wants/collect_shutdown_logs.service
	ln -sf ${systemd_unitdir}/system/collect_shutdown_logs.service ${D}${systemd_unitdir}/system/halt.target.wants/collect_shutdown_logs.service
	ln -sf ${systemd_unitdir}/system/collect_default_logs.service ${D}${systemd_unitdir}/system/reboot.target.wants/collect_default_logs.service
	ln -sf ${systemd_unitdir}/system/collect_default_logs.service ${D}${systemd_unitdir}/system/poweroff.target.wants/collect_default_logs.service
	ln -sf ${systemd_unitdir}/system/collect_default_logs.service ${D}${systemd_unitdir}/system/halt.target.wants/collect_default_logs.service
}

FILES:${PN} += "${sysconfdir}/"
FILES:${PN} += "${systemd_unitdir}/system/"