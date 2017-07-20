DESCRIPTION = "Start up script for mhi_swip config"
HOMEPAGE = "http://codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI +="file://${BASEMACHINE}/config_swip_net.sh"

S = "${WORKDIR}/${BASEMACHINE}"

PR = "r4"

inherit update-rc.d

INITSCRIPT_NAME   = "config_swip_net.sh"
INITSCRIPT_PARAMS = "start 99 5 ."

do_install() {
    install -m 0755 ${WORKDIR}/${BASEMACHINE}/config_swip_net.sh -D ${D}${sysconfdir}/init.d/config_swip_net.sh
}

pkg_postinst_${PN} () {
        update-alternatives --install ${sysconfdir}/init.d/config_swip_net.sh config_swip_net.sh config_swip_net.sh 60
        [ -n "$D" ] && OPT="-r $D" || OPT="-s"
        # remove all rc.d-links potentially created from alternatives
        update-rc.d $OPT -f config_swip_net.sh remove
        update-rc.d $OPT config_swip_net.sh multiuser
}
