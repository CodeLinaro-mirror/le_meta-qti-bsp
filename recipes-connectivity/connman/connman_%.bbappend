
inherit update-alternatives
ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} ="resolv-conf"
ALTERNATIVE_TARGET[resolv-conf] = "${sysconfdir}/resolv-conf.connman"
ALTERNATIVE_LINK_NAME[resolv-conf] = "${sysconfdir}/resolv.conf"

do_fix_connman_resolv_conf() {
    # For read-only filesystem, do not create links during bootup
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        if ${@bb.utils.contains('IMAGE_FEATURES','read-only-rootfs','true','false',d)}; then
            echo "d    /var/run/connman    - - - -" > ${D}${sysconfdir}/tmpfiles.d/connman_resolvconf.conf
        fi
        ln -sf ../run/connman/resolv.conf ${D}${sysconfdir}/resolv-conf.connman
    fi
}

addtask fix_connman_resolv_conf after do_install before do_package

