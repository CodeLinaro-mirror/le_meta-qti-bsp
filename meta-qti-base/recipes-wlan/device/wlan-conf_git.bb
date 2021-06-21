SUMMARY = "Device Config File"
DESCRIPTION = "Device specific config"
HOMEPAGE = "https://www.codeaurora.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
SRCREV = "${AUTOREV}"
PR = "r0"

SRC_URI = "${PATH_TO_REPO}/device/qcom/wlan/.git;protocol=${PROTO};destsuffix=device/qcom/wlan;usehead=1"

S = "${WORKDIR}/device"

do_install(){
    install -d ${D}${sysconfdir}/misc/wifi
    install -m 0644 ${S}/qcom/wlan/msm_auto/*.conf ${D}${sysconfdir}/misc/wifi
    install -d ${D}${bindir}
    install -m 0755 ${S}/qcom/wlan/msm_auto/*.sh ${D}${bindir}
}
