do_install() {
    mkdir -p ${D}${QML_LIBDIR}/execScript/
    cp execScript/*.so ${D}${QML_LIBDIR}/execScript/
    cp ../git/execScript/qmldir ${D}${QML_LIBDIR}/execScript/
}


FILES_${PN} += "/usr/lib/qt5/qml/execScript/libexecscriptplugin.so"
FILES_${PN} += "/usr/lib/qt5/qml/execScript/qmldir"
FILES_${PN}-dbg += "/usr/lib/qt5/qml/execScript/.debug"
