FILESEXTRAPATHS_append := ":${THISDIR}/systemd-225"

SRC_URI_append += "file://70-net-setup-link.rules \
                   file://DWC_ETH_QOS.conf \
                   file://60-persistent-v4l.rules"

do_install_append () {
  install -m 0644 ${WORKDIR}/70-net-setup-link.rules ${D}${sysconfdir}/udev/rules.d/
  install -m 0644 ${WORKDIR}/DWC_ETH_QOS.conf ${D}${sysconfdir}/modules-load.d/
  install -m 0644 ${WORKDIR}/60-persistent-v4l.rules ${D}${sysconfdir}/udev/rules.d/
}
