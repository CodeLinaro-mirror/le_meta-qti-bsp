pkg_postinst_${PN} () {
    sed -e '/^hosts:/s/\<localuser\>\s*//' \
        -e 's/\(^hosts:\s\s*\)\(.*\)/\1localuser \2/' \
        -i $D${sysconfdir}/nsswitch.conf
}

pkg_postinst_ontarget_${PN} () {
}

pkg_prerm_${PN} () {
}
