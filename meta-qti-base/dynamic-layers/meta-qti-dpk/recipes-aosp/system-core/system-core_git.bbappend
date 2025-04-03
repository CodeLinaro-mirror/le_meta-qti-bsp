do_install:append() {
    # replace all '/sbin' to '${SBIN}', and dynamic determine SBIN's path
    sed -i 's;/sbin;${SBIN};g' ${D}${sysconfdir}/initscripts/usb
    sed -i '26 a\SBIN=/vendor/sbin\n[ -f /sbin/usb/target ] && SBIN=/sbin\n' ${D}${sysconfdir}/initscripts/usb
    # replace all '/etc' to '${ETC}', and dynamic determine ETC's path
    sed -i 's;/etc;${ETC};g' ${D}${sysconfdir}/initscripts/usb
    sed -i '29 a\ETC=/vendor/etc\n[ -f /etc/initscripts/usb ] && ETC=/etc\n' ${D}${sysconfdir}/initscripts/usb

    # add '/vendor' at path for dpk varient
    sed -i 's;/sbin;/vendor/sbin;g' ${D}${base_sbindir}/usb_composition
    sed -i 's;/etc;/vendor/etc;g' ${D}${base_sbindir}/usb_composition
    sed -i 's;/sbin;/vendor/sbin;g' ${D}${base_sbindir}/usb_debug

    # add '/vendor' at path for usb composition
    find ${D}${base_sbindir}/usb/compositions -type f -exec sed -i 's;/sbin/usb/target;/vendor/sbin/usb/target;g' {} \;
}
