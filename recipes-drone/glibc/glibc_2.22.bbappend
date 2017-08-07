FILES_${PN} += "/lib/ld-linux-armhf.so.3"

do_install_append() {
    if [ "${MLPREFIX}" == "lib32-" ] || [ "${MLPREFIX}" == "" -a "${TUNE_ARCH}" == "arm" ]; then
        cd ${D}/lib
        ln -sf ld-linux.so.3 ld-linux-armhf.so.3
        cd -
    fi
}
