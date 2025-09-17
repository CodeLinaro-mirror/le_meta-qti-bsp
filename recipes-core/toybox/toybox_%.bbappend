do_configure:append() {
    # Enable mdev
    sed -e 's/# CONFIG_MDEV is not set/CONFIG_MDEV=y/' -i .config

    # Enable mdev conf
    sed -e 's/# CONFIG_MDEV_CONF is not set/CONFIG_MDEV_CONF=y/' -i .config

    sed -e 's/# CONFIG_SWAPON is not set/CONFIG_SWAPON=y/' -i .config
    sed -e 's/CONFIG_EGREP=y/# CONFIG_EGREP is not set/' -i .config
    sed -e 's/CONFIG_FGREP=y/# CONFIG_FGREP is not set/' -i .config
    sed -e 's/CONFIG_GREP=y/# CONFIG_GREP is not set/' -i .config
    sed -e 's/CONFIG_HALT=y/# CONFIG_HALT is not set/' -i .config
    sed -e 's/CONFIG_CP=y/# CONFIG_CP is not set/' -i .config
}

do_install:append(){
	sed -i '/^\/sbin\/halt$/d' ${D}/etc/toybox.links
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
