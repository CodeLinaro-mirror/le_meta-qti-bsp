include wpa-supplicant.inc

PR = "${INC_PR}.2"

SRC_URI="${CAF_LA_GIT}/platform/external/wpa_supplicant_8.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=external/wpa_supplicant_8"
SRC_URI += "file://defconfig-qcacld"

DEPENDS += "qmi"
DEPENDS += "qmi-framework"

S = "${WORKDIR}/external/wpa_supplicant_8/wpa_supplicant"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}
