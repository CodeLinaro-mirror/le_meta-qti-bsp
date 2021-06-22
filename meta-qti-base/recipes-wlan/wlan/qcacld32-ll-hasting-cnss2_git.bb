require qcacld32-ll.inc

SUMMARY = "Qualcomm Atheros WLAN Driver"
DESCRIPTION = "Qualcomm Atheros WLAN CLD3.0 low latency driver"
HOMEPAGE = "https://www.codeaurora.org/"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"
SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "qcacld_cmn_fw_msm"
PR = "r8"

_MODNAME = "qca6390"
FW_PATH_NAME = "qca6390"
FIRMWARE_PATH = "${D}${nonarch_base_libdir}/firmware/wlan/qca_cld/${_MODNAME}"

SRC_URI = "${PATH_TO_REPO}/wlan/qcacld-3.0/.git;protocol=${PROTO};destsuffix=wlan/qcacld-3.0;usehead=1 \
           ${PATH_TO_REPO}/wlan/qca-wifi-host-cmn/.git;protocol=${PROTO};destsuffix=wlan/qca-wifi-host-cmn;usehead=1 \
           ${PATH_TO_REPO}/wlan/fw-api/.git;protocol=${PROTO};destsuffix=wlan/fw-api/;usehead=1 \
           ${PATH_TO_REPO}/device/qcom/wlan/.git;protocol=${PROTO};destsuffix=device/qcom/wlan/msm_auto;subpath=msm_auto;usehead=1 \
           file://init_qti_wlan_auto.service \
           file://init.qti.wlan_on.sh \
           file://init.qti.wlan_off.sh \
           "

S1 = "${WORKDIR}/wlan/qca-wifi-host-cmn"
S = "${WORKDIR}/wlan/qcacld-3.0"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE += "CONFIG_CLD_HL_SDIO_CORE=n CONFIG_CNSS_SDIO=n"
EXTRA_OEMAKE += "CONFIG_QCA_CLD_WLAN_PROFILE=qca6390"
EXTRA_OEMAKE += "DYNAMIC_SINGLE_CHIP=${_MODNAME}"
EXTRA_OEMAKE += "MULTI_IF_NAME=cnss2"
EXTRA_OEMAKE += "MODNAME=${_MODNAME}"
EXTRA_OEMAKE_append_qtiquingvm += "WLAN_CFG_OVERRIDE="CONFIG_WLAN_DISABLE_EXPORT_SYMBOL=y CONFIG_WLAN_OPEN_P2P_INTERFACE=n CONFIG_SUPPORT_P2P_BY_ONE_INTF_WLAN=y CONFIG_WLAN_PLACEMARKER_PREFIX=108 CONFIG_FEATURE_GPIO_CFG=y CONFIG_CNSS_GENL=n CONFIG_MULTI_IF_LOG=y CONFIG_FEATURE_WLAN_CH_AVOID_EXT=y""
EXTRA_OEMAKE_append_qtiquingvm8295 += "WLAN_CFG_OVERRIDE="CONFIG_WLAN_DISABLE_EXPORT_SYMBOL=y CONFIG_WLAN_OPEN_P2P_INTERFACE=n CONFIG_SUPPORT_P2P_BY_ONE_INTF_WLAN=y CONFIG_WLAN_PLACEMARKER_PREFIX=108""

do_configure_append() {
    sed -i -e 's/^gEnableConcurrentSTA=wlan1/gEnableConcurrentSTA=wlan3/g' ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qca6390.ini
    sed -i '1 i\host_log_custom_nl_proto=1' ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qca6390.ini
}

do_install() {
    module_do_install

    install -d ${FIRMWARE_PATH}
    install -d ${D}${includedir}/qcacld/
    install -m 0644 ${S1}/utils/nlink/inc/wlan_nlink_common.h ${D}${includedir}/qcacld/

    #copying wlan.ko to STAGING_DIR_TARGET
    WLAN_KO=${@oe.utils.conditional('PERF_BUILD', '1', '${STAGING_DIR_TARGET}-perf', '${STAGING_DIR_TARGET}', d)}
    install -d ${WLAN_KO}/wlan
    install -m 0644 ${S}/${_MODNAME}.ko ${WLAN_KO}/wlan/

    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qca6390.ini ${FIRMWARE_PATH}/WCNSS_qcom_cfg.ini
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/wlan_mac_hst_2.bin ${FIRMWARE_PATH}/wlan_mac.bin
    install -d ${D}${bindir}
    install -D -m 0755 ${WORKDIR}/init.qti.wlan_on.sh ${D}${bindir}/init.qti.wlan_on.sh
    install -D -m 0755 ${WORKDIR}/init.qti.wlan_off.sh ${D}${bindir}/init.qti.wlan_off.sh

    install -d ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/amss.bin ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/amss20.bin ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.e01 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.e02 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan.elf ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/m3.bin ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/

    # Install systemd service file
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/init_qti_wlan_auto.service -D ${D}${systemd_unitdir}/system/init_qti_wlan_auto.service
    fi
}
