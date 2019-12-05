FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
            file://rsyslog.conf \
            file://rsyslog.logrotate \
            file://rsyslog_rotate.sh \
            file://logrotate.crontab \
"
FILES_${PN} += "${sysconfdir}/crontabs/rsyslog.logrotate ${sysconfdir}/rsyslog.d/rsyslog_rotate.sh ${sysconfdir}/rsyslog.conf"
RDEPENDS_${PN} += "busybox-crond"

do_install_append() {
    install -d 644 ${D}${sysconfdir}/rsyslog.d
    install -d 644 ${D}${sysconfdir}/crontabs
    install -m 644 ${WORKDIR}/logrotate.crontab ${D}${sysconfdir}/crontabs/root
    install -m 755 ${WORKDIR}/rsyslog_rotate.sh ${D}${sysconfdir}/rsyslog.d/rsyslog_rotate.sh
    install -m 644 ${WORKDIR}/rsyslog.logrotate ${D}${sysconfdir}/logrotate.d/logrotate.rsyslog
    install -m 644 ${WORKDIR}/rsyslog.conf ${D}${sysconfdir}/rsyslog.conf
}