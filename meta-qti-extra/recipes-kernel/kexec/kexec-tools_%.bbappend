FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://kdump-qti "

do_install_append() {
    echo "KDUMP_CMDLINE=\"minidump=1 maxcpus=1 initcall_debug earlycon=msm_geni_serial,0xa90000 clk_ignore_unused rcupdate.rcu_expedited=1 rcu_nocbs=0-7 root=/dev/ram rw rootwait console=ttyMSM0,115200,n8 lpm_levels.sleep_disabled=1 nokaslr 1 reset_devices minidump=1 androidboot.slot_suffix=_a\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    echo "KDUMP_VMCORE_PATH=\"/data/crash/\`date +"%Y-%m-%d"\`\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    sed -i "s/^MAKEDUMPFILE_ARGS.*$/MAKEDUMPFILE_ARGS=\"-d 31 -c\"/g" ${D}${sysconfdir}/sysconfig/kdump.conf
    sed -i "s/^\#KDUMP_KVER/KDUMP_KVER/g" ${D}${sysconfdir}/sysconfig/kdump.conf
    sed -i "s/^\#KDUMP_KIMAGE=\"\/boot\/bzImage/KDUMP_KIMAGE=\"\/boot\/Image/g" ${D}${sysconfdir}/sysconfig/kdump.conf

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        sed -i "s/^After=sysinit.target/After=sysinit.target data.mount/g" ${D}${systemd_unitdir}/system/kdump.service
        sed -i "s/^WantedBy=multi-user.target/WantedBy=sysinit.target/g" ${D}${systemd_unitdir}/system/kdump.service
        sed -i "s/kdump-helper/kdump-qti-helper/g" ${D}${systemd_unitdir}/system/kdump.service
        sed -i "/^DefaultDependencies/a\ConditionVirtualization=!container" ${D}${systemd_unitdir}/system/kdump.service
        install -D -m 0755 ${WORKDIR}/kdump-qti ${D}${libexecdir}/kdump-qti-helper
    fi
}
