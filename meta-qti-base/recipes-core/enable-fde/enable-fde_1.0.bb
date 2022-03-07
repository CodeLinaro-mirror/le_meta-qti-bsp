SUMMARY = "Enable FDE on data partition"
DESCRIPTION = "Partition name which wants to encrypt will send through hab channel to host and host will generate key and encrypt the partition"
HOMEPAGE = "http://www.codeaurora.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "\
           file://enable-fde.sh \
           file://enable-fde.service \
"

inherit systemd

SYSTEMD_SERVICE_${PN} = "enable-fde.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install_append () {
  install -d ${D}${systemd_system_unitdir}
  install -d ${D}${bindir}
  install -m 0755 ${WORKDIR}/enable-fde.sh ${D}${bindir}/enable-fde.sh
  install -m 0644 ${WORKDIR}/enable-fde.service ${D}${systemd_unitdir}/system/
}

RDEPENDS_${PN} += "e2fsprogs-mke2fs"
