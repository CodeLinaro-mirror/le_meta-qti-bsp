FILESEXTRAPATHS_append := ":${THISDIR}/connman-conf"

SRC_URI = "file://main.conf \
           file://tether_ethernet.config"

FILES_${PN} += "${sysconfdir}/connman/*"

do_install_append () {
  install -d ${D}${sysconfdir}/connman/
  install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/
  install -m 0644 ${WORKDIR}/tether_ethernet.config ${D}${sysconfdir}/connman/
}
