do_install_append_8x96autocv2x() {
    # APQ8096AU uses ks_bridge built-in driver for Sahara so
    # remove the rule for usbserial to load the usb serial module
    rm ${D}${sysconfdir}/udev/rules.d/90-telematics-usb.rules
}
