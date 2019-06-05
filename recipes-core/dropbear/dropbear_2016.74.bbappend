do_install_append(){
  sed -i "s/DROPBEAR_RSAKEY_DIR=\/etc\/dropbear/DROPBEAR_RSAKEY_DIR=\/var\/lib\/dropbear/g" ${D}${systemd_unitdir}/system/dropbearkey.service
  sed -i "s/DROPBEAR_RSAKEY_DIR=\/etc\/dropbear/DROPBEAR_RSAKEY_DIR=\/var\/lib\/dropbear/g" ${D}${systemd_unitdir}/system/dropbear@.service
}
