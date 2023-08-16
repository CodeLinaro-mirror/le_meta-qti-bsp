FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
# Fetch from the codelinaro location. 
SRC_URI = "${CLO_LE_GIT}/kmod.git;protocol=https;branch=caf_migration/kmod/master"
SRC_URI += "file://depmod-search.conf \
            file://avoid_parallel_tests.patch \
            file://fix-O_CLOEXEC.patch \
            file://blacklist.conf \
           "

do_install_append () {
    install -Dm644 "${WORKDIR}/blacklist.conf" "${D}${sysconfdir}/modprobe.d/blacklist.conf"
}
