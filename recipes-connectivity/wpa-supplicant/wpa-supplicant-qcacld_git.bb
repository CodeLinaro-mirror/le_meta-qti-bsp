include wpa-supplicant.inc

PR = "${INC_PR}.2"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://external/wpa_supplicant_8/"
SRC_URI += "file://defconfig-qcacld"

DEPENDS += "qmi"
DEPENDS += "qmi-framework"
FILES_${PN} += "/usr/include/*"

S = "${WORKDIR}/external/wpa_supplicant_8/wpa_supplicant"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
    eval $(awk '$2=="VERSION_STR" {printf("WAP_VER=%s;",$3)}' ${WORKDIR}/external/wpa_supplicant_8/src/common/version.h)
    if [ $WAP_VER == "2.9-devel" ]; then
        echo "CONFIG_OWE=y" >>.config
        echo "CONFIG_SAE=y" >>.config
    fi
}
