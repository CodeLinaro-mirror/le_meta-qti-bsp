do_install:append () {
    if [ -f ${WORKDIR}/01-key-conf.conf ]; then
        rm -f ${D}${systemd_unitdir}/logind.conf.d/01-key-conf.conf
    fi
}
