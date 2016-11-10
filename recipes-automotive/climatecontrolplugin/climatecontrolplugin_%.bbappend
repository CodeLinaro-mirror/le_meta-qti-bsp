PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install () {
        mkdir -p ${D}${libdir}/qt5/qml/Automotive/ClimateControl/
        cp libClimateControl.so ${D}${libdir}/qt5/qml/Automotive/ClimateControl/
        cp qmldir ${D}${libdir}/qt5/qml/Automotive/ClimateControl/
}

FILES_${PN} += "/usr/lib/qt5/qml/Automotive/ClimateControl/libClimateControl.so \
                /usr/lib/qt5/qml/Automotive/ClimateControl/qmldir \
               "
FILES_${PN}-dbg += "/usr/lib/qt5/qml/Automotive/ClimateControl/.debug"
