do_install:append() {
    # replace all '/sbin' to '${SBIN}', and dynamic determine SBIN's path
    sed -i 's;/sbin;${SBIN};g' ${D}${sysconfdir}/initscripts/usb
    sed -i '26 a\SBIN=/vendor/sbin\n[ -f /sbin/usb/target ] && SBIN=/sbin\n' ${D}${sysconfdir}/initscripts/usb
    # replace all '/etc' to '${ETC}', and dynamic determine ETC's path
    sed -i 's;/etc;${ETC};g' ${D}${sysconfdir}/initscripts/usb
    sed -i '29 a\ETC=/vendor/etc\n[ -f /etc/initscripts/usb ] && ETC=/etc\n' ${D}${sysconfdir}/initscripts/usb
}

