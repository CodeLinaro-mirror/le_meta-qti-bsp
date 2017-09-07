DESCRIPTION = "Network configuration files for systemd"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI += "file://dsrc.network"
SRC_URI += "file://mhi_swip.network"
SRC_URI += "file://wlan.network"
SRC_URI += "file://wpa_supplicant@wlan0.service"
SRC_URI += "file://wpa_supplicant-wlan0.conf"

FILES_${PN} += "${sysconfdir}/systemd/network/"
FILES_${PN} += "${sysconfdir}/systemd/system/"
FILES_${PN} += "${sysconfdir}/wpa_supplicant/"

do_install() {
    # install network configuration files
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/*.network ${D}${sysconfdir}/systemd/network/
    # systemd service for wpa_supplicant
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    install -m 0644 ${WORKDIR}/wpa_supplicant@wlan0.service ${D}${sysconfdir}/systemd/system/
    ln -sf ${sysconfdir}/systemd/system/wpa_supplicant@wlan0.service \
        ${D}${sysconfdir}/systemd/system/multi-user.target.wants/wpa_supplicant@wlan0.service
    # wpa_supplicant conf
    install -d ${D}${sysconfdir}/wpa_supplicant
    install -m 0644 ${WORKDIR}/wpa_supplicant-wlan0.conf ${D}${sysconfdir}/wpa_supplicant
}
