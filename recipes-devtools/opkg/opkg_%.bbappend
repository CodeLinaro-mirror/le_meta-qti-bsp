# for read-only-rootfs disable opkg service at bootup

SYSTEMD_AUTO_ENABLE_${PN} = "${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs",'disable', 'enable', d)}"
