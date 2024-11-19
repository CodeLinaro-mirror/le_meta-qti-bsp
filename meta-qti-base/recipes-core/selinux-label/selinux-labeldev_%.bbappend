do_install:append() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'true', 'false', d)}; then
        sed -i 's/Before=sysinit.target/Before=sysinit.target systemd-modules-load.service vfio-device-probe.service/' ${D}${systemd_unitdir}/system/selinux-labeldev.service
    fi
}

