do_install:append() {
        rm ${D}${systemd_unitdir}/network/80-wired.network

        #Override default setting and use powerkey to do suspend on qti platform.
        echo "HandlePowerKey=suspend" >> ${D}${systemd_unitdir}/logind.conf.d/00-${PN}.conf
}

