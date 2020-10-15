DESCRIPTION = "Wi-Fi Protected Access(WPA) Supplicant"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"
DEPENDS = "openssl libnl dbus qmi qmi-framework"
SRCREV = "${AUTOREV}"
PR = "r5.2"

SRC_URI = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1 \
           file://wpa_supplicant.conf-sane \
           file://defconfig-qcacld"

SOLIBS = "*.so"
FILES_SOLIBSDEV = ""

S = "${WORKDIR}/external/wpa_supplicant_8/wpa_supplicant"

inherit autotools-brokensep linux-kernel-base pkgconfig

do_configure() {
    sed -i -e 's/^CONFIG_EAP_PROXY=qmi/#CONFIG_EAP_PROXY=qmi/g' ${WORKDIR}/defconfig-qcacld
    sed -i -e 's/^CONFIG_EAP_PROXY_DUAL_SIM := true/#CONFIG_EAP_PROXY_DUAL_SIM := true/g' ${WORKDIR}/defconfig-qcacld
    sed -i -e 's/^CONFIG_EAP_PROXY_AKA_PRIME := true/#CONFIG_EAP_PROXY_AKA_PRIME := true/g' ${WORKDIR}/defconfig-qcacld
    #enable CONFIG_WNM
    if (( `grep -c "^CONFIG_WNM=y" ${WORKDIR}/defconfig-qcacld` )); then
        break
    elif (( `grep -c "^#CONFIG_WNM=y" ${WORKDIR}/defconfig-qcacld` )); then
        sed -i -e 's/^#CONFIG_WNM=y/CONFIG_WNM=y/g' ${WORKDIR}/defconfig-qcacld
    else
        sed -i '$a\CONFIG_WNM=y' ${WORKDIR}/defconfig-qcacld
    fi

    #enable CONFIG_WIFI_DISPLAY
    if (( `grep -c "^CONFIG_WIFI_DISPLAY=y" ${WORKDIR}/defconfig-qcacld` )); then
        break
    elif (( `grep -c "^#CONFIG_WIFI_DISPLAY=y" ${WORKDIR}/defconfig-qcacld` )); then
        sed -i -e 's/^#CONFIG_WIFI_DISPLAY=y/CONFIG_WIFI_DISPLAY=y/g' ${WORKDIR}/defconfig-qcacld
    else
        sed -i '$a\CONFIG_WIFI_DISPLAY=y' ${WORKDIR}/defconfig-qcacld
    fi

    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}
do_install() {
    make install DESTDIR=${D} BINDIR=${sbindir} LIBDIR=${libdir} INCDIR=${includedir}

    install -d ${D}${docdir}/wpa_supplicant
    install -m 644 ${S}/wpa_supplicant.conf ${D}${docdir}/wpa_supplicant

    install -d ${D}${sysconfdir}
    install -m 600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant.conf
}

CONFFILES_${PN} += "${sysconfdir}/wpa_supplicant.conf"
