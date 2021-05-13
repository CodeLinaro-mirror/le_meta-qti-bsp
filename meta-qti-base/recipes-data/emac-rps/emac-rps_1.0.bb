SUMMARY = "RPS script to achieve ethernet peak troughputs"
DESCRIPTION = "Qualcomm Technologies, Inc. Binary to setting RPS(Receive Packet Steering) value for ethernet peak throughputs"
HOMEPAGE = "https://git.codelinaro.org"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "\
    file://emac_rps.service \
    file://emac_rps_settings.sh \
"

inherit systemd

SYSTEMD_SERVICE:${PN} = "emac_rps.service"

do_install() {
    install -D -m 0755 ${WORKDIR}/emac_rps_settings.sh ${D}${bindir}/emac_rps_settings.sh
    install -D -m 0644 ${WORKDIR}/emac_rps.service ${D}${systemd_unitdir}/system/emac_rps.service
}
