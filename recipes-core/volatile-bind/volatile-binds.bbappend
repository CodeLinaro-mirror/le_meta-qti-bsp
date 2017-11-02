# Configure read-only-rootfs RW backend for /var/lib

SYSTEMD_AUTO_ENABLE_${PN} = "${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs",'disable', 'enable', d)}"

pkg_postinst_${PN} () {
    if ${@bb.utils.contains('IMAGE_FEATURES','read-only-rootfs','true','false',d)}; then
        echo "/persist/var-lib     /var/lib             auto       defaults,bind         0  0" >> $D/etc/fstab
    fi
}
