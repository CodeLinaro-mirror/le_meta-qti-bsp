SRC_URI+= "file://tether_ethernet.config"

do_install_append () {
  install -m 0644 ${WORKDIR}/tether_ethernet.config ${D}${sysconfdir}/connman/
}
