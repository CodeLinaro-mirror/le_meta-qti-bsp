RDEPENDS_${PN} += " mobile-broadband-provider-info"

do_install_prepend() {
    touch ${WORKDIR}/ofono
}
