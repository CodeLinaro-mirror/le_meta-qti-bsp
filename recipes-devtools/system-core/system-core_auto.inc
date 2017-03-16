EXTRA_OECONF_remove = "--with-logd-logging"

do_install_append() {
   if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
      sed -i -e '/^ExecStart=/i\ExecStartPre=/bin/mkdir -p -m 777 /dev/socket' ${D}/${systemd_unitdir}/system/leprop.service
   fi
}
