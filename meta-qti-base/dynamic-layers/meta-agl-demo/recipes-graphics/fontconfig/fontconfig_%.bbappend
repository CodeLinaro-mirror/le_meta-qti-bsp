do_install:append() {
    # Remove /var/cache/fontconfig from configuration to
    # quiet errors on AGL application startup
    sed -i 's|.*<cachedir>/var/cache/fontconfig</cachedir>.*||' ${D}${sysconfdir}/fonts/fonts.conf
}
