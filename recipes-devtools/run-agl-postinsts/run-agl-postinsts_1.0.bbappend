do_install_append() {
        sed -i -e '/^ExecStartPost/d'  ${D}${systemd_unitdir}/system/run-agl-postinsts.service
        sed -i -e '/^Type=oneshot/a\ExecStartPre=/bin/chown -R cynara:cynara /var/lib/cynara/'   ${D}${systemd_unitdir}/system/run-agl-postinsts.service
        sed -i -e 's/\/etc\/agl-postinsts/\/var\/lib\/agl-postinsts/'  ${D}${systemd_unitdir}/system/run-agl-postinsts.service
        sed -i -e 's/\/etc\/agl-postinsts/\/var\/lib\/agl-postinsts/'  ${D}${sbindir}/run-agl-postinsts
}

