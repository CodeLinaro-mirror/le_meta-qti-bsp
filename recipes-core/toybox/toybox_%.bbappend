do_configure:append() {
    # Enable mdev
    sed -e 's/# CONFIG_MDEV is not set/CONFIG_MDEV=y/' -i .config

    # Enable mdev conf
    sed -e 's/# CONFIG_MDEV_CONF is not set/CONFIG_MDEV_CONF=y/' -i .config

    sed -e 's/# CONFIG_SWAPON is not set/CONFIG_SWAPON=y/' -i .config
}

do_install:append(){
      cp -rp ${D}/etc/toybox.links ${D}/etc/toybox.links.ramdisk
	sed -i '/^\/sbin\/halt$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/cp$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/insmod$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/login$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/lsmod$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/rmmod$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/modinfo$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/mount$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/su$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/umount$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/poweroff$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/reboot$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/swapoff$/d' ${D}/etc/toybox.links
	sed -i '/^\/sbin\/swapon$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/chattr$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/lsattr$/d' ${D}/etc/toybox.links
	sed -i '/^\/usr\/bin\/base32$/d' ${D}/etc/toybox.links
	sed -i '/^\/bin\/cat$/d' ${D}/etc/toybox.links

}
