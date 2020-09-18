DESCRIPTION = "Device specific config"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"


# Provide a baseline
SRC_URI = "git://source.codeaurora.org/platform/vendor/qcom/wlan.git;protocol=https;destsuffix=device/qcom/wlan;nobranch=1"
# Update for each machine
S = "${WORKDIR}/device"
SRCREV = "a72009409705ecfa3c2a6e22e8c767679869191e"

do_install_append_auto(){
	install -d ${D}/etc/misc/wifi
	install -m 0644 ${S}/qcom/wlan/sdx_auto/*.conf ${D}/etc/misc/wifi
	install -d ${D}/usr/bin
	install -m 0755 ${S}/qcom/wlan/sdx_auto/*.sh ${D}/usr/bin
}

do_install_append_automotive(){
	install -d ${D}/etc/misc/wifi
	install -m 0644 ${S}/qcom/wlan/msm_auto/*.conf ${D}/etc/misc/wifi
	install -d ${D}/usr/bin
	install -m 0755 ${S}/qcom/wlan/msm_auto/*.sh ${D}/usr/bin
}
