FILESEXTRAPATHS_append := ":${THISDIR}/systemd-229"

SRC_URI_append += "file://70-net-setup-link.rules \
                   file://60-persistent-v4l.rules"

do_install_append () {
  install -m 0644 ${WORKDIR}/70-net-setup-link.rules ${D}${sysconfdir}/udev/rules.d/
  install -m 0644 ${WORKDIR}/60-persistent-v4l.rules ${D}${sysconfdir}/udev/rules.d/
}
