require qcacld32-ll.inc

DESCRIPTION = "Qualcomm Atheros WLAN CLD3.0 low latency driver"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"
SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "qcacld_cmn_fw_msm"
PR = "r8"

_MODNAME = "qca6574"
FW_PATH_NAME = "qca6174"
FIRMWARE_PATH = "${D}${nonarch_base_libdir}/firmware/wlan/qca_cld/${_MODNAME}"

SRC_URI = "${PATH_TO_REPO}/wlan/qcacld-3.0/.git;protocol=${PROTO};destsuffix=wlan/qcacld-3.0;usehead=1 \
           ${PATH_TO_REPO}/wlan/qca-wifi-host-cmn/.git;protocol=${PROTO};destsuffix=wlan/qca-wifi-host-cmn;usehead=1 \
           ${PATH_TO_REPO}/wlan/fw-api/.git;protocol=${PROTO};destsuffix=wlan/fw-api/;usehead=1 \
           ${PATH_TO_REPO}/device/qcom/wlan/.git;protocol=${PROTO};destsuffix=device/qcom/wlan/msm_auto;subpath=msm_auto;usehead=1 \
           file://init_qti_wlan_auto.service \
           file://init.qti.wlan_on.sh \
           file://init.qti.wlan_off.sh \
           "



S1 = "${WORKDIR}/wlan/qca-wifi-host-cmn/"
S = "${WORKDIR}/wlan/qcacld-3.0/"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE += "CONFIG_CLD_HL_SDIO_CORE=n CONFIG_CNSS_SDIO=n"
EXTRA_OEMAKE += "CONFIG_QCA_CLD_WLAN_PROFILE=qca6174"
EXTRA_OEMAKE += "DYNAMIC_SINGLE_CHIP=${_MODNAME}"
EXTRA_OEMAKE += "MODNAME=${_MODNAME}"
EXTRA_OEMAKE += "CONFIG_AR6320_SUPPORT=y"

do_install() {
    module_do_install

    install -d ${FIRMWARE_PATH}
    install -d ${D}${includedir}/qcacld/
    install -m 0644 ${S1}/utils/nlink/inc/wlan_nlink_common.h ${D}${includedir}/qcacld/

    #copying wlan.ko to STAGING_DIR_TARGET
    WLAN_KO=${@oe.utils.conditional('PERF_BUILD', '1', '${STAGING_DIR_TARGET}-perf', '${STAGING_DIR_TARGET}', d)}
    install -d ${WLAN_KO}/wlan
    install -m 0644 ${S}/${_MODNAME}.ko ${WLAN_KO}/wlan/

    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qca6174.ini ${FIRMWARE_PATH}/WCNSS_qcom_cfg.ini
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/wlan_mac.bin ${FIRMWARE_PATH}/wlan_mac.bin
    install -d ${D}${bindir}
    install -D -m 0755 ${WORKDIR}/init.qti.wlan_on.sh ${D}${bindir}/init.qti.wlan_on.sh
    install -D -m 0755 ${WORKDIR}/init.qti.wlan_off.sh ${D}${bindir}/init.qti.wlan_off.sh

    install -d ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan30.b00 ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan30.bin ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    mv ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/bdwlan30.bin ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/utfbd30.bin
    ln -sf /firmware/image/${FW_PATH_NAME}/qwlan30.bin ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/utf30.bin ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/otp30.bin ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/data.msc ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan30.b31 ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    mv ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/bdwlan30.b31 ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/utfbd30.b31
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan30.bin ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan30.b31 ${D}${nonarch_base_libdir}/firmware/${_MODNAME}/

    # Install systemd service file
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/init_qti_wlan_auto.service -D ${D}${systemd_unitdir}/system/init_qti_wlan_auto.service
    fi
}
