inherit module qperf systemd

DESCRIPTION = "Qualcomm Atheros WLAN CLD3.0 low latency driver"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

FILES_${PN}     += "lib/firmware/wlan/*"
FILES_${PN}     += "${base_libdir}/modules/${KERNEL_VERSION}/extra/wlan.ko"
PROVIDES_NAME   = "kernel-module-wlan"
RPROVIDES_${PN} += "${PROVIDES_NAME}"

do_unpack[deptask] = "do_populate_sysroot"
PR = "r8"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://wlan/qcacld-3.0/"
SRC_URI += "file://wlan/qca-wifi-host-cmn/"
SRC_URI += "file://wlan/fw-api/"
SRC_URI += "file://device/qcom/wlan/romelv/WCNSS_qcom_cfg.ini"

S = "${WORKDIR}/wlan/qcacld-3.0/"
S_STRIPPED = "${WORKDIR}/packages-split/kernel-module-wlan/lib/modules/${KERNEL_VERSION}/extra"

FIRMWARE_PATH = "${D}/lib/firmware/wlan/qca_cld"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE += "CONFIG_CLD_HL_SDIO_CORE=n CONFIG_CNSS_SDIO=n"

# The common header file, 'wlan_nlink_common.h' can be installed from other
# qcacld recipes too. To suppress the duplicate detection error, add it to
# SSTATE_DUPWHITELIST.
SSTATE_DUPWHITELIST += "${STAGING_DIR}/${MACHINE}${includedir}/qcacld/wlan_nlink_common.h"

FILES_${PN} += "/lib/firmware/"

SRC_URI += "file://init_qti_wlan.service"
SYSTEMD_SERVICE_${PN} = "init_qti_wlan.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
SYSTEMD_AUTO_ENABLE_${PN}_8x96autocv2x = "disable"

do_compile_prepend () {
    if [ "${MACHINE}" = "8x96autocv2x" ]; then
        sed -i -e 's/QDF_LOCK_STATS_BUG_ON=1/QDF_LOCK_STATS_BUG_ON=0/g' ${S}/Kbuild
    fi
}

do_install () {
    module_do_install

    install -d ${FIRMWARE_PATH}
    install -d ${D}${includedir}/qcacld/
    install -m 0644 ${S}/../qca-wifi-host-cmn/utils/nlink/inc/wlan_nlink_common.h ${D}${includedir}/qcacld/

    # Copying wlan.ko to STAGING_DIR_TARGET
    WLAN_KO=${@base_conditional('PERF_BUILD', '1', '${STAGING_DIR_TARGET}-perf', '${STAGING_DIR_TARGET}', d)}
    install -d ${WLAN_KO}/wlan
    install -m 0644 ${S}/wlan.ko ${WLAN_KO}/wlan/

    if [ "${MACHINE}" = "8x96autocv2x" ]; then
        sed -i -e 's/BandCapability=0/BandCapability=1/g' ${WORKDIR}/device/qcom/wlan/romelv/WCNSS_qcom_cfg.ini
        sed -i 's/^END/fw_timeout_crash=0\nEND/g' ${WORKDIR}/device/qcom/wlan/romelv/WCNSS_qcom_cfg.ini
    fi

    install -d ${D}/lib/firmware/wlan/qca_cld
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/romelv/WCNSS_qcom_cfg.ini ${D}/lib/firmware/wlan/qca_cld/

    # Install systemd service file
    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/init_qti_wlan.service -D ${D}${systemd_unitdir}/system/init_qti_wlan.service
    fi
}

do_module_signing() {
    KMOD_SIG_ALL=`cat ${STAGING_KERNEL_BUILDDIR}/.config | grep CONFIG_MODULE_SIG_ALL | cut -d'=' -f2`
    KMOD_SIG_HASH=`cat ${STAGING_KERNEL_BUILDDIR}/.config | grep CONFIG_MODULE_SIG_HASH | cut -d'=' -f2 | sed 's/\"//g'`
    if [ "$KMOD_SIG_ALL" = "y" ] && [ -n "$KMOD_SIG_HASH" ]; then
        if [ "${KERNEL_VERSION:0:3}" = "4.4" ]; then
            MODSECKEY=${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem
            MODPUBKEY=${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509
        else
            MODSECKEY=${STAGING_KERNEL_BUILDDIR}/signing_key.priv
            MODPUBKEY=${STAGING_KERNEL_BUILDDIR}/signing_key.x509
        fi

        if [ "${KERNEL_VERSION:0:3}" = "4.4" ]; then
            ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file $KMOD_SIG_HASH $MODSECKEY $MODPUBKEY ${S_STRIPPED}/wlan.ko
        else
            perl ${STAGING_KERNEL_DIR}/scripts/sign-file $KMOD_SIG_HASH $MODSECKEY $MODPUBKEY ${S_STRIPPED}/wlan.ko
        fi
    fi;
}

addtask module_signing after do_package before do_package_write_ipk
