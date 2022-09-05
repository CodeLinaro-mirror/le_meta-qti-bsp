inherit autotools pkgconfig systemd

DESCRIPTION = "Andorid like properties managment for LE"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/system/core/:"
SRC_URI = "file://leproperties"

S = "${WORKDIR}/leproperties"

DEPENDS += "libselinux libcutils liblog"

EXTRA_OECONF = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '',d)}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'full-disk-encryption', '--with-fulldiskencryption', '',d)}"
do_install_append() {
    if ${@bb.utils.contains('EXTRA_OECONF', '--with-systemd', 'true', 'false', d)}; then
        install -b -m 0644 /dev/null -D ${D}${sysconfdir}/build.prop
        install -d ${D}${systemd_unitdir}/system/
        install -d ${D}${systemd_unitdir}/system/ffbm.target.wants/
        if ${@bb.utils.contains('EXTRA_OECONF', '--with-fulldiskencryption', 'true', 'false', d)}; then
            install -d ${D}${systemd_unitdir}/system/local-fs-pre.target.requires/
            ln -sf ${systemd_unitdir}/system/leprop.service \
                   ${D}${systemd_unitdir}/system/local-fs-pre.target.requires/leprop.service
        else
            install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
            ln -sf ${systemd_unitdir}/system/leprop.service \
                   ${D}${systemd_unitdir}/system/multi-user.target.wants/leprop.service
        fi
        ln -sf ${systemd_unitdir}/system/leprop.service \
               ${D}${systemd_unitdir}/system/ffbm.target.wants/leprop.service
    fi
}

FILES_${PN} += "${systemd_unitdir}/system/"
