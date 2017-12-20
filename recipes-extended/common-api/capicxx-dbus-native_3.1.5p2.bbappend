do_install_append() {
    cd ${D}${bindir}
    rm ${LAUNCHER_LINK}
    ln -sf -T "../share/capicxx-dbus-native-3.1.5p2/${LAUNCHER}" ${LAUNCHER_LINK}
}

