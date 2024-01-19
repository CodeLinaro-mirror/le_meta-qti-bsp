FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
             file://0032-systemd-add-bootkpi-marker-for-user-session.patch \
             ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'file://0034-systemd-add-handover-support-for-early-service.patch', '', d)} \
             file://power-switch.rules \
             file://qti_sleep.sh \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'file://0001-systemd-sleep-change-suspend-state-list.patch', '', d)} \
             ${@bb.utils.contains('MACHINE_FEATURES', 'deepsleep', 'file://0002-systemd-add-deepsleep-support.patch', '', d)} "

do_install:append() {
   install -d ${D}/${base_libdir}/systemd/system-sleep
   install -m 0755 ${WORKDIR}/qti_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh
   ${@bb.utils.contains('BASEMACHINE', 'sa81x5', 'sed -i -e \'64a\    sleep 2\' ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh','', d)}
}
