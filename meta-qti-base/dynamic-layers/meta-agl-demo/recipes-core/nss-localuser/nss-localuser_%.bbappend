pkg_postinst:${PN} () {
    sed -e '/^hosts:/s/\<localuser\>\s*//' \
        -e 's/\(^hosts:\s\s*\)\(.*\)/\1localuser \2/' \
        -i $D${sysconfdir}/nsswitch.conf
}

pkg_postinst_ontarget:${PN} () {
}

pkg_prerm:${PN} () {
}
