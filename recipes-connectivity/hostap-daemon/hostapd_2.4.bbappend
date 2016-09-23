DESCRIPTION = "Hostap Daemon"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILESEXTRAPATHS_prepend := "${WORKSPACE}:"
SRC_URI = "file://external/wpa_supplicant_8/ \
           file://defconfig \
           file://init \
           file://hostapd.service \
          "
S = "${WORKDIR}/external/wpa_supplicant_8/hostapd"

do_configure_prepend () {
    #Chnage defconfig file to configure specific features
    sed -i -e 's/^CONFIG_DRIVER_HOSTAP=y/#CONFIG_DRIVER_HOSTAP=y/g' ${WORKDIR}/defconfig
    sed -i -e 's/^CONFIG_DRIVER_WIRED=y/#CONFIG_DRIVER_WIRED=y/g' ${WORKDIR}/defconfig
    sed -i -e 's/^CONFIG_DRIVER_PRISM54=y/#CONFIG_DRIVER_PRISM54=y/g' ${WORKDIR}/defconfig
}
