DESCRIPTION = "Start up script for finding partitions used in recovery"
HOMEPAGE = "http://codeaurora.org"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
LICENSE = "BSD"

SRC_URI +="file://${BASEMACHINE}/find_recovery_partitions.sh"
SRC_URI +="file://find-recovery-partitions.service"
S = "${WORKDIR}/${BASEMACHINE}"

PR = "r3"

inherit update-rc.d

INITSCRIPT_NAME = "find_recovery_partitions.sh"
INITSCRIPT_PARAMS_8x96auto = "start 01 5 ."
INITSCRIPT_PARAMS_8x96autofusion = "start 01 5 ."
INITSCRIPT_PARAMS_8x96autonapier = "start 01 5 ."

do_install() {
    install -m 0755 ${WORKDIR}/${BASEMACHINE}/find_recovery_partitions.sh -D ${D}${sysconfdir}/init.d/find_recovery_partitions.sh
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/find-recovery-partitions.service -D ${D}${systemd_unitdir}/system/find-recovery-partitions.service
    install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
     # enable the service for sysinit.target
     ln -sf ${systemd_unitdir}/system/find-recovery-partitions.service \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/find-recovery-partitions.service
}

FILES_${PN} += "/lib/"
FILES_${PN} += "${systemd_unitdir}/system/"
