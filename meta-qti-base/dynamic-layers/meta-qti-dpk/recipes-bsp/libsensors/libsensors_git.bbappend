do_install:append() {
    sed -i "s;/etc;vendor/etc;g" ${D}${sysconfdir}/udev/rules.d/61-sensor.rules
    sed -i "s;/etc;vendor/etc;g" ${D}${sysconfdir}/udev/rules.d/61-iio.rules
}

