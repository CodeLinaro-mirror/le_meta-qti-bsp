DESCRIPTION = "Start up script for resetting userdata partition to factory state"
HOMEPAGE = "http://codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI  = "file://userfs-reset"
SRC_URI += "file://mount-data"
SRC_URI += "file://mount-data.service"

S = "${WORKDIR}/"

PR = "r1"

inherit systemd
inherit update-rc.d

INITSCRIPT_NAME = "mount-data"
INITSCRIPT_PARAMS = "start 99 5 . stop 99 0 6 ."

FILES_${PN} += "${systemd_unitdir}/system/"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}
    then
        install -m 0750 ${WORKDIR}/mount-data \
                -D ${D}${sysconfdir}/initscripts/mount-data
        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/mount-data.service \
                -D ${D}${systemd_unitdir}/system/mount-data.service
        install -d ${D}${systemd_unitdir}/system/local-fs.target.requires/
        ln -sf ${systemd_unitdir}/system/mount-data.service \
               ${D}${systemd_unitdir}/system/local-fs.target.requires/mount-data.service
    else
        install -m 0750 ${WORKDIR}/mount-data \
                 -D ${D}${sysconfdir}/init.d/mount-data
    fi

    install -m 0750 ${WORKDIR}/userfs-reset -D ${D}${bindir}/userfs-reset
}
