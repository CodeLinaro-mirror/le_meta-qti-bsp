inherit autotools update-rc.d systemd

DESCRIPTION = "Installing audio init script"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
PR = "r5"

DEPENDS_append_mdm9635 +="alsa-intf"

#re-use non-perf settings
BASEMACHINE = "${@d.getVar('MACHINE', True).replace('-perf', '')}"

SRC_URI = " \
            file://init_qcom_audio \
            file://init_qcom_audio.service \
"
SRC_URI_msm8974 = "file://${BASEMACHINE}/init_qcom_audio"
SRC_URI_msm8610 = "file://${BASEMACHINE}/init_qcom_audio"

S = "${WORKDIR}"
S_msm8974 = "${WORKDIR}/${BASEMACHINE}"
S_msm8610 = "${WORKDIR}/${BASEMACHINE}"

INITSCRIPT_NAME_8x96auto = "init_qcom_audio"
INITSCRIPT_PARAMS_8x96auto = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autofusion = "init_qcom_audio"
INITSCRIPT_PARAMS_8x96autofusion = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autogvmquin = "init_qcom_audio"
INITSCRIPT_PARAMS_8x96autogvmquin = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autogvmred = "init_qcom_audio"
INITSCRIPT_PARAMS_8x96autogvmred = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_msm8996 = "init_qcom_audio"
INITSCRIPT_PARAMS_msm8996 = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96mctm = "init_qcom_audio"
INITSCRIPT_PARAMS_8x96mctm = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_msm8974 = "init_qcom_audio"
INITSCRIPT_PARAMS_msm8974 = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_msm8610 = "init_qcom_audio"
INITSCRIPT_PARAMS_msm8610 = "start 99 2 3 4 5 . stop 1 0 1 6 ."

SYSTEMD_SERVICE_${PN} = "init_qcom_audio.service"

do_install() {
    #install the init.d/init_qcom_audio
    install -m 0755 ${S}/init_qcom_audio -D ${D}${sysconfdir}/init.d/init_qcom_audio

    #install systemd service file
    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0755 ${S}/init_qcom_audio -D ${D}${base_bindir}/init_qcom_audio
        install -m 0644 ${S}/init_qcom_audio.service -D ${D}${systemd_unitdir}/system/init_qcom_audio.service
    fi
}

do_install_msm8974() {
    install -m 0755 ${S}/${BASEMACHINE}/init_qcom_audio -D ${D}${sysconfdir}/init.d/init_qcom_audio
}

do_install_msm8610() {
    install -m 0755 ${S}/${BASEMACHINE}/init_qcom_audio -D ${D}${sysconfdir}/init.d/init_qcom_audio
}
