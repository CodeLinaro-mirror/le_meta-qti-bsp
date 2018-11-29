# set afm dir to /var/lib/ if read-only-rootfs is enabled
afm_datadir = "${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs",'/data/var/lib/${afm_name}', '/var/local/lib/${afm_name}', d)}"
systemd_units_root = "${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs",'/home/root/.config/systemd', '/var/local/lib/systemd', d)}"

FILES_${PN} += " ${afm_datadir} "
