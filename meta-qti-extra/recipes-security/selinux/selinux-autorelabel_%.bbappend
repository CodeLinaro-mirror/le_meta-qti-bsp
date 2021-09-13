do_install_append() {
	#selinux-autorelabel.sh is used to label filesystem when first boot up, need to mount with rw options, or labelling would fail.
	sed -i "/\${SETENFORCE} 0/a\ \ \ \ \ \ \ \ mount / -o rw,remount" ${D}/usr/bin/selinux-autorelabel.sh
}
