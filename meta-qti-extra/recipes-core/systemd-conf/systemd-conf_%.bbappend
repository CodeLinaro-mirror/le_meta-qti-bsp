do_install_append () {
   #Override default setting and use powerkey to do suspend on qti platform.
   echo "HandlePowerKey=suspend" >> ${D}${systemd_unitdir}/logind.conf.d/00-${PN}.conf
}
