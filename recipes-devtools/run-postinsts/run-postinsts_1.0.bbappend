
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

inherit systemd

SYSTEMD_SERVICE:${PN} = "run-postinsts.service"

do_install:append:qcs610-odk-64() {
    install -m 0644 ${WORKDIR}/run-postinsts.service ${D}${systemd_unitdir}/system/run-postinsts.service

    sed -i 's|ExecStartPost=#BASE_BINDIR#/systemctl disable run-postinsts.service|ExecStartPost=#BASE_BINDIR#/systemctl --no-reload disable run-postinsts.service|' \
        ${D}${systemd_unitdir}/system/run-postinsts.service
}

