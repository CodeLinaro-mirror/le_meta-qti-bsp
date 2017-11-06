inherit autotools pkgconfig systemd

DESCRIPTION = "Installing audio init script"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
PR = "r5"

DEPENDS_append_mdm9635 +="alsa-intf"

SRC_URI = "file://init_qcom_audio"
SRC_URI += "file://init_audio.service"

S = "${WORKDIR}"

INITSCRIPT_NAME = "init_qcom_audio"
INITSCRIPT_PARAMS = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_PARAMS_apq8009 = "start 38 2 3 4 5 . stop 1 0 1 6 ."

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0755 ${S}/init_qcom_audio -D ${D}${sysconfdir}/initscripts/init_qcom_audio
        install -d ${D}/etc/systemd/system/
        install -m 0755 ${S}/init_audio.service -D ${D}${sysconfdir}/systemd/system/init_audio.service
        install -d ${D}/etc/systemd/system/multi-user.target.wants/
        ln -sf /etc/systemd/system/init_audio.service \
              ${D}/etc/systemd/system/multi-user.target.wants/init_audio.service
    else
        install -m 0755 ${S}/init_qcom_audio -D ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
    fi

}

pkg_postinst_${PN} () {

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
        [ -n "$D" ] && OPT="-r $D" || OPT="-s"
        update-rc.d $OPT -f ${INITSCRIPT_NAME} remove
        update-rc.d $OPT ${INITSCRIPT_NAME} ${INITSCRIPT_PARAMS}
    fi
}

FILES_${PN} += "${systemd_unitdir}/system/"
