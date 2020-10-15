FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
# Fetch from the CAF location. 
SRC_URI = "git://source.codeaurora.org/quic/le/kmod.git;protocol=https;branch=kmod/master"
SRC_URI += "file://depmod-search.conf \
            file://avoid_parallel_tests.patch \
            file://fix-O_CLOEXEC.patch \
            file://blacklist.conf \
           "

do_install_append () {
    install -Dm644 "${WORKDIR}/blacklist.conf" "${D}${sysconfdir}/modprobe.d/blacklist.conf"
}
