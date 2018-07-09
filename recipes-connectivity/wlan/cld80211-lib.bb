inherit autotools qcommon

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_DIR = "${WORKSPACE}/hardware/qcom/wlan/cld80211-lib/"
S = "${WORKDIR}/hardware/qcom/wlan/cld80211-lib"

DEPENDS += "libnl"
