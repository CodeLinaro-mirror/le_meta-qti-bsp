include hostap-daemon.inc

PR = "${INC_PR}.2"

SRC_URI="${CAF_LA_GIT}/platform/external/wpa_supplicant_8.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=external/wpa_supplicant_8"
SRC_URI += "file://defconfig-qcacld"

S = "${WORKDIR}/external/wpa_supplicant_8/hostapd/"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}

