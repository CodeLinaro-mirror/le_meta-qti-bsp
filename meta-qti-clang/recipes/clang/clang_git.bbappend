do_install_append_class-native () {

    # FIXME: Workaround for cfi_blacklist.txt check after CONFIG_CFI_CLANG is enabled
    if [ ! -f ${D}${nonarch_libdir}/clang/${PV}/share/cfi_blacklist.txt ]; then
        mkdir -p ${D}${nonarch_libdir}/clang/${PV}/share
        touch ${D}${nonarch_libdir}/clang/${PV}/share/cfi_blacklist.txt
    fi

}
