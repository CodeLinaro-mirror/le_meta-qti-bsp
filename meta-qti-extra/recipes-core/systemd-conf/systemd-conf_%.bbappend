FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://90-powerkey-conf.conf"

do_install:append() {
    # Use powerkey to do suspend on qti platform.
    install -d ${D}${systemd_unitdir} ${D}${systemd_unitdir}/logind.conf.d
    install -m 0644 ${WORKDIR}/90-powerkey-conf.conf ${D}${systemd_unitdir}/logind.conf.d
}
