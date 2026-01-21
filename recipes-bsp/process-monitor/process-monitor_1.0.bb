inherit autotools systemd

LICENSE          = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"

DESCRIPTION = "process monitor"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/QPlatformUtils:"
SRC_URI += "file://process-monitor"

DEPENDS += "systemd jsoncpp faultmgr"
RDEPENDS:${PN} += "faultmgr bash"

SYSTEMD_SERVICE_${PN}  = "process-monitor.service"
SYSTEMD_AUTO_ENABLE    = "enable"
S = "${WORKDIR}/${PN}"

do_install_append() {
    
    # Install the systemd unit
    install -m 0755 ${S}/process-monitor.service -D ${D}${systemd_unitdir}/system/process-monitor.service
    
    # Deploy JSON configuration
    install -m 0644 ${S}/conf/process_monitor-${MACHINE}.json -D ${D}${sysconfdir}/process_monitor-${MACHINE}.json
    
    # Deploy D-Bus policy
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${S}/conf/process_monitor-dbus.conf ${D}${sysconfdir}/dbus-1/system.d/

}

FILES:${PN} += " \
  ${bindir}/process-monitor \
  ${systemd_unitdir}/system/process-monitor.service \
  ${sysconfdir}/process_monitor-${MACHINE}.json \
  ${sysconfdir}/dbus-1/system.d/process_monitor-dbus.conf \
"