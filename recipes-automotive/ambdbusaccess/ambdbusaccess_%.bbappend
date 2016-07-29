do_install () {
        mkdir -p ${D}${libdir}/qt5/qml/Automotive/
        cp ambdbusaccess ${D}${libdir}/qt5/qml/Automotive/
}

FILES_${PN} += "/usr/lib/qt5/qml/Automotive/ambdbusaccess"
FILES_${PN}-dbg += "/usr/lib/qt5/qml/Automotive/.debug"
