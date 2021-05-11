# Disable QTI weston.service, use AGL's weston@.service instead
SYSTEMD_PACKAGES = ""

do_install_append() {
    # Remove CAF weston.ini to avoid conflict with AGL weston-ini-conf package
    rm -f ${D}${sysconfdir}/xdg/weston/weston.ini

    # Remove weston.service added by previous bbappend
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        rm -f ${D}${systemd_system_unitdir}/weston.service
    fi
}
