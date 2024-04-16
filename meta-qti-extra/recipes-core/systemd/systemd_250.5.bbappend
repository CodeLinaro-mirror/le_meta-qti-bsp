FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
             file://0032-systemd-add-bootkpi-marker-for-user-session.patch \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'file://0001-systemd-assign-prime-core-to-manager_dispatch_load_q.patch', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'file://0034-systemd-add-handover-support-for-early-service.patch', '', d)} \
             file://power-switch.rules \
             file://qti_sleep.sh \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'file://0001-systemd-sleep-change-suspend-state-list.patch', '', d)} \
             ${@bb.utils.contains('MACHINE_FEATURES', 'deepsleep', 'file://0002-systemd-add-deepsleep-support.patch', '', d)} \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah', '', 'file://qti_lxc_umd_sleep.sh', d), '', d)}"

do_install:append() {
   install -d ${D}/${base_libdir}/systemd/system-sleep
   install -m 0755 ${WORKDIR}/qti_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh
   if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)} ; then
      if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah', 'false', 'true', d)} ; then
         install -m 0755 ${WORKDIR}/qti_lxc_umd_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_lxc_umd_sleep.sh
      fi
   fi
}
