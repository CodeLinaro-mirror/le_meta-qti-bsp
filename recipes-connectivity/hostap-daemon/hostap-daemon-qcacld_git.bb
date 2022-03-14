include hostap-daemon.inc

PR = "${INC_PR}.2"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://external/wpa_supplicant_8/"
SRC_URI += "file://defconfig-qcacld"

S = "${WORKDIR}/external/wpa_supplicant_8/hostapd/"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
    eval $(awk '$2=="VERSION_STR" {printf("WAP_VER=%s;",$3)}' ${WORKDIR}/external/wpa_supplicant_8/src/common/version.h)
    if [ $WAP_VER == "2.9-devel" ] || [ $WAP_VER == "2.10-devel" ]; then
        echo "CONFIG_OWE=y" >>.config
        echo "CONFIG_SAE=y" >>.config
    fi
}

