
LICENSE          = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"

DESCRIPTION = "Repair common partitions(persist, data, systemrw, manifest and cache) in case of header corruption"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/QPlatformUtils:"
SRC_URI = "file://${BPN}"

S = "${WORKDIR}/${BPN}"
RDEPENDS_${PN} = "dump e2fsprogs"

do_install_append() {
    install -d ${D}/persist_bkp
    install -d ${D}/data_bkp
    install -d ${D}${sysconfdir}/
    install -d ${D}${systemd_unitdir}/system/
    install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
    install -d ${D}${systemd_unitdir}/system/local-fs.target.wants/

    # install partition-recovery.service and enable in systemd
    install -m 0644 ${S}/hlos-partition-repair.service -D ${D}${systemd_unitdir}/system/hlos-partition-repair.service
    ln -sf ${systemd_unitdir}/system/hlos-partition-repair.service ${D}${systemd_unitdir}/system/local-fs.target.wants/hlos-partition-repair.service

    # install persist-backup.service and enable in systemd
    install -m 0644 ${S}/persist-backup.service -D ${D}${systemd_unitdir}/system/persist-backup.service
    ln -sf ${systemd_unitdir}/system/persist-backup.service ${D}${systemd_unitdir}/system/multi-user.target.wants/persist-backup.service

    # install persist-backup.timer and enable in systemd
    install -m 0644 ${S}/persist-backup.timer -D ${D}${systemd_unitdir}/system/persist-backup.timer
    ln -sf ${systemd_unitdir}/system/persist-backup.timer ${D}${systemd_unitdir}/system/multi-user.target.wants/persist-backup.timer

    # install scripts with executable permission in /etc
    install -m 0644 ${S}/hlos-partition-repair.conf -D ${D}${sysconfdir}/hlos-partition-repair.conf
    install -m 0755 ${S}/hlos-partition-repair.sh -D ${D}${sysconfdir}/hlos-partition-repair.sh
    install -m 0755 ${S}/persist-backup.sh -D ${D}${sysconfdir}/persist-backup.sh
}

# Add systemd and etc in package
FILES_${PN} += "${systemd_unitdir}/system/"
FILES_${PN} += "${sysconfdir}/"
FILES_${PN} += "/persist_bkp"
FILES_${PN} += "/data_bkp"
