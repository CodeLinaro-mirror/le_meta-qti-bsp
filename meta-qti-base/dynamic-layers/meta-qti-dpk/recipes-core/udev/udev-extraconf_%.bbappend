do_install:append() {
    # Do not mount /dev/vd* for hypervisor
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        sed -i "$ a\/dev\/vd*" ${D}${sysconfdir}/udev/mount.ignorelist
    fi
}
