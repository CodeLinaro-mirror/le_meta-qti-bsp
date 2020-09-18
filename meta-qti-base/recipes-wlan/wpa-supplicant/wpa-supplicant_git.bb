inherit pkgconfig
include wpa-supplicant.inc

PR = "${INC_PR}.2"

SRC_URI   = "git://source.codeaurora.org/platform/external/wpa_supplicant_8.git;protocol=https;destsuffix=external/wpa_supplicant_8;nobranch=1"
SRC_URI_append = " file://defconfig-qcacld"
SRCREV = "1344cc76644be54b58ab059622aaccb04ef3492b"

DEPENDS += "qmi"
DEPENDS += "qmi-framework"
FILES_${PN} += "/usr/include/*"

S = "${WORKDIR}/external/wpa_supplicant_8/wpa_supplicant"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}

INCSUFFIX ?= "none"
INCSUFFIX_automotive = "wpa-supplicant_auto"
include ${INCSUFFIX}.inc
