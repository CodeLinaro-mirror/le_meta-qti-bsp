DESCRIPTION = "Enable fde on data partition"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit systemd

SRC_URI +="file://enable-fde.sh"
SRC_URI +="file://enable-fde.service"

INITSCRIPT_NAME = "enable-fde.sh"

SYSTEMD_SERVICE_${PN} = "enable-fde.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install_append () {
  install -d ${D}${systemd_system_unitdir}
  install -d ${D}${bindir}
  install -m 0755 ${WORKDIR}/enable-fde.sh ${D}${bindir}/enable-fde.sh
  install -m 0644 ${WORKDIR}/enable-fde.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${systemd_unitdir}/system/enable-fde.service"
FILES_${PN} += "/usr/bin/enable-fde.sh"
