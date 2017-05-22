FILESEXTRAPATHS_append := ":${THISDIR}/systemd-229"

# 0001-sysv-generator-add-default-dependencies.patch depends on 
# 0013-sysv-generator-add-support-for-executing-scripts-und.patch
SRC_URI_append += "file://70-net-setup-link.rules \
                   file://60-persistent-v4l.rules \
                   file://0001-sysv-generator-add-default-dependencies.patch"

do_install_append () {
  install -m 0644 ${WORKDIR}/70-net-setup-link.rules ${D}${sysconfdir}/udev/rules.d/
  install -m 0644 ${WORKDIR}/60-persistent-v4l.rules ${D}${sysconfdir}/udev/rules.d/
}
