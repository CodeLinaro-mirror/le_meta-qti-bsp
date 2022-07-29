do_install:append() {
    install -d ${D}${sysconfdir}/modules-load.d
    echo vcan > ${D}${sysconfdir}/modules-load.d/vcan.conf
}
