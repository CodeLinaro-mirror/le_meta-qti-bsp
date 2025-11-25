FILESBBAPPENDPATH := "${THISDIR}"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BP}:${FILESBBAPPENDPATH}/${BPN}:"

SRC_URI:append = " \
    file://60-misc.rules \
    file://power-switch.rules \
    file://qti_sleep.sh \
"

SRC_URI:append = " ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "6.12", "file://linux-msm-6.12_modules_load.conf", "", d)}"

do_install:append () {
    # Use kernel rules for network iface name
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}${systemd_unitdir}/network/99-default.link

    install -d ${D}/${base_libdir}/systemd/system-sleep
    install -m 0755 ${WORKDIR}/qti_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh

    if ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "6.12", "true", "false", d)}; then
        install -m 0664 ${WORKDIR}/linux-msm-6.12_modules_load.conf ${D}${sysconfdir}/modules-load.d/
    fi
}
