do_install:append() {
    install -d ${D}${sysconfdir}/systemd/system/init_codec2.service.d
    cat > ${D}${sysconfdir}/systemd/system/init_codec2.service.d/override.conf <<EOF
[Unit]
After=smack-rule-loader.service
[Install]
WantedBy=multi-user.target
EOF
}

FILES:${PN} += "\    
    ${sysconfdir}/systemd/system/init_codec2.service.d/override.conf \
"
