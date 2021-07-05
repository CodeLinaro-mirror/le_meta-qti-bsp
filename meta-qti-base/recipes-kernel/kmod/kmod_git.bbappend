FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://blacklist.conf"

do_install_append () {
    install -Dm644 "${WORKDIR}/blacklist.conf" "${D}${sysconfdir}/modprobe.d/blacklist.conf"
}
