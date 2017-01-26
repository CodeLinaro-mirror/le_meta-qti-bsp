SUMMARY = "neutrino-eth"

DESCRIPTION = "neutrino ethernet"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "virtual/kernel"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:"

SRC_URI = "file://vehiclenetwork/ethernet"
SRC_URI += "file://neutrino-eth.service"
SRC_URI += "file://neutrino-eth-8x96autofusion.service"

PR = "r0"
PV = "0.1"

S =  "${WORKDIR}/ethernet"

INHIBIT_PACKAGE_STRIP = "1"

inherit module systemd

SYSTEMD_SERVICE_${PN} = "neutrino-eth.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_compile_append () {
    KMOD_SIG_ALL=`cat ${TMPDIR}/work/${MACHINE}-poky-linux/linux-yocto-msm8996/3.18-r2/linux-${MACHINE}-standard-build/.config | grep CONFIG_MODULE_SIG_ALL | cut -d'=' -f2`
    KMOD_SIG_HASH=`cat ${TMPDIR}/work/${MACHINE}-poky-linux/linux-yocto-msm8996/3.18-r2/linux-${MACHINE}-standard-build/.config | grep CONFIG_MODULE_SIG_HASH | cut -d'=' -f2 | sed 's/\"//g'`
    if [ "$KMOD_SIG_ALL" = "y" ] && [ -n "$KMOD_SIG_HASH" ]; then
        MODSECKEY=${TMPDIR}/work/${MACHINE}-poky-linux/linux-yocto-msm8996/3.18-r2/linux-${MACHINE}-standard-build/signing_key.priv
        MODPUBKEY=${TMPDIR}/work/${MACHINE}-poky-linux/linux-yocto-msm8996/3.18-r2/linux-${MACHINE}-standard-build/signing_key.x509
        cp ${S}/DWC_ETH_QOS.ko ${S}/DWC_ETH_QOS.ko.unsigned
        perl ${TMPDIR}/work/${MACHINE}-poky-linux/linux-yocto-msm8996/3.18-r2/kernel/scripts/sign-file $KMOD_SIG_HASH $MODSECKEY $MODPUBKEY ${S}/DWC_ETH_QOS.ko
    fi;
}


do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}/etc
    install ${WORKDIR}/ethernet/config.ini ${D}/etc/ntn_config.ini
    if [ "${MACHINE}" == "8x96autofusion" ]; then
        install -m 0644 ${WORKDIR}/neutrino-eth-8x96autofusion.service ${D}${systemd_unitdir}/system/neutrino-eth.service
    else
        install -m 0644 ${WORKDIR}/neutrino-eth.service ${D}${systemd_unitdir}/system/neutrino-eth.service
    fi
}

FILES_${PN} += "${systemd_unitdir}/system/neutrino-eth.service \
                /etc/* \
"
