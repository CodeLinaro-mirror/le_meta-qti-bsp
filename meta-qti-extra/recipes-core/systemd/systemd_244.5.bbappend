FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0032-systemd-add-bootkpi-marker-for-user-session.patch \
    ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'file://0034-systemd-add-handover-support-for-early-service.patch', '', d)} \
    file://power-switch.rules \
    file://qti_sleep.sh \
"

do_install:append() {
    install -d ${D}/${base_libdir}/systemd/system-sleep
    install -m 0755 ${WORKDIR}/qti_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh
    # Skip pam_selinux check to systemd-user as workaround the issue that user@0 service can't start up
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        sed -i '/pam_selinux/s/^/#/g' ${D}${sysconfdir}/pam.d/systemd-user
    fi
}
