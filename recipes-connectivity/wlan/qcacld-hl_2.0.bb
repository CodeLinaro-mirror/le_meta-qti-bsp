inherit module kernel-arch qperf

PACKAGE_ARCH = "${MACHINE_ARCH}"

DESCRIPTION = "Qualcomm Atheros WLAN CLD high latency driver"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

python __anonymous () {
     d.setVar('WLAN_MODULE_NAME', 'dsrc')
     d.setVar('CHIP_NAME', 'qca6584')
}

FILES_${PN}     += "/lib/firmware/"
FILES_${PN}     += "${base_libdir}/modules/${KERNEL_VERSION}/extra/${WLAN_MODULE_NAME}.ko"

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.
RPROVIDES_${PN} += "${@'kernel-module-${WLAN_MODULE_NAME}'.replace('_', '-')}"
PROVIDES_NAME   = "kernel-module-${WLAN_MODULE_NAME}"

do_unpack[deptask] = "do_populate_sysroot"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://wlan/qcacld-2.0/"
S = "${WORKDIR}/wlan/qcacld-2.0"

INHIBIT_PACKAGE_STRIP = "1"

EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"
EXTRA_OEMAKE += "CONFIG_CLD_HL_SDIO_CORE=y CONFIG_BUS_AUTO_SUSPEND=n"
EXTRA_OEMAKE += "MODNAME=${WLAN_MODULE_NAME} CHIP_NAME=${CHIP_NAME}"

# The common header file, 'wlan_nlink_common.h' can be installed from other
# qcacld recipes too. To suppress the duplicate detection error, add it to
# SSTATE_DUPWHITELIST.
SSTATE_DUPWHITELIST += "${STAGING_DIR}/${MACHINE}${includedir}/qcacld/wlan_nlink_common.h"

do_compile_append () {
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

        cp ${S}/${WLAN_MODULE_NAME}.ko ${S}/${WLAN_MODULE_NAME}.ko.unsigned

        if [ "${KERNEL_VERSION:0:3}" = "4.4" ]; then
            ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file $KMOD_SIG_HASH $MODSECKEY $MODPUBKEY ${S}/${WLAN_MODULE_NAME}.ko
        else
            perl ${STAGING_KERNEL_DIR}/scripts/sign-file $KMOD_SIG_HASH $MODSECKEY $MODPUBKEY ${S}/${WLAN_MODULE_NAME}.ko
        fi
    fi;
}

do_install () {
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra
    install -D -m 0644 ${S}/${WLAN_MODULE_NAME}.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/

    # Enable 802.11p (DSRC) standalone mode
    sed -i -e 's/^END/# OCB mode - 1=standalone\ngDot11PMode=1\n\nEND/g' ${S}/firmware_bin/WCNSS_qcom_cfg.ini

    install -d ${D}/lib/firmware/wlan/qca_cld/${CHIP_NAME}
    install -D -m 0644 ${S}/firmware_bin/WCNSS_qcom_cfg.ini ${D}/lib/firmware/wlan/qca_cld/${CHIP_NAME}/
    install -D -m 0644 ${S}/firmware_bin/WCNSS_cfg.dat ${D}/lib/firmware/wlan/qca_cld/${CHIP_NAME}/
}
