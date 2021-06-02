FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://smack-adbd \
    file://smack.conf \
    "

do_install_append() {
    install -D -m 0644 ${WORKDIR}/smack-adbd ${D}${sysconfdir}/smack/accesses.d/adbd

    mkdir -p ${D}${systemd_unitdir}/system/adbd.service.d
    install -m 0644 ${WORKDIR}/smack.conf ${D}${systemd_unitdir}/system/adbd.service.d/
}

FILES_${PN}-adbd += "\
    ${systemd_unitdir}/system/adbd.service.d/smack.conf \
    ${sysconfdir}/smack/accesses.d/adbd \
    "
