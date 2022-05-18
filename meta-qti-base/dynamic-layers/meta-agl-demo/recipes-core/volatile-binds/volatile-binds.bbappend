FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://smack-reload.conf \
"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d -m 0755 ${D}${systemd_unitdir}/system/var-smack-accesses.d.service.d
        install -m 0644 ${WORKDIR}/smack-reload.conf ${D}${systemd_unitdir}/system/var-smack-accesses.d.service.d/
    fi
}

FILES:${PN} += "${systemd_unitdir}/system/var-smack-accesses.d.service.d/"
