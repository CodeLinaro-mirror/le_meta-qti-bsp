FILESEXTRAPATHS_append := " :${THISDIR}/weston/"
SRC_URI = "file://weston.service_caf \
           file://weston.ini_caf \
"
SYSTEMD_SERVICE_${PN} = "weston.service"
SYSTEMD_AUTO_ENABLE = "enable"

REQUIRED_DISTRO_FEATURES_remove = "opengl"

do_install() {
    # Install systemd unit files
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)}; then
            install -m 644 -p -D ${WORKDIR}/weston_early.service_caf ${D}${systemd_system_unitdir}/weston.service
        else
            install -m 644 -p -D ${WORKDIR}/weston.service_caf ${D}${systemd_system_unitdir}/weston.service
        fi
    fi

    install -D -p -m0644 ${WORKDIR}/weston.ini_caf ${D}${sysconfdir}/xdg/weston/weston.ini
    # Install reuqire-input=false in weston.ini
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        sed -i -e '/\[core\]/a require-input=false' ${D}${sysconfdir}/xdg/weston/weston.ini
    fi
}
