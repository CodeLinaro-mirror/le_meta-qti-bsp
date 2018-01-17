inherit update-rc.d systemd

#re-use non-perf settings
BASEMACHINE = "${@d.getVar('MACHINE', True).replace('-perf', '')}"

SRC_URI = " \
            file://${BASEMACHINE}/init_qti_audio \
            file://${BASEMACHINE}/init_qti_audio.service \
"
S = "${WORKDIR}/${BASEMACHINE}"

SYSTEMD_SERVICE_${PN} = "init_qti_audio.service"

do_install() {
    #install the init.d/init_qti_audio
    install -m 0755 ${S}/init_qti_audio -D ${D}${sysconfdir}/init.d/init_qti_audio

    #install systemd service file
    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0755 ${S}/init_qti_audio -D ${D}${base_bindir}/init_qti_audio
        install -m 0644 ${S}/init_qti_audio.service -D ${D}${systemd_unitdir}/system/init_qti_audio.service
    fi
}
