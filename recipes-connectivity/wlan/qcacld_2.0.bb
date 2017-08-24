DESCRIPTION = "wifi drivers for QCA6574A-1 on AGL platform"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://wlan/qcacld-2.0/ \
           file://wifi_on.sh \
           file://wifi_off.sh \
           file://init_qti_wlan.service \
          "
DEPENDS = "virtual/kernel"

S = "${WORKDIR}/wlan/qcacld-2.0"

inherit module kernel-arch qperf
inherit module update-rc.d
INITSCRIPT_NAME = "wifi_on.sh"

inherit systemd
SYSTEMD_SERVICE_${PN} = "init_qti_wlan.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

INHIBIT_PACKAGE_STRIP = "1"

EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

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

        cp ${S}/wlan.ko ${S}/wlan.ko.unsigned

        if [ "${KERNEL_VERSION:0:3}" = "4.4" ]; then
            ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file $KMOD_SIG_HASH $MODSECKEY $MODPUBKEY ${S}/wlan.ko
        else
            perl ${STAGING_KERNEL_DIR}/scripts/sign-file $KMOD_SIG_HASH $MODSECKEY $MODPUBKEY ${S}/wlan.ko
        fi
    fi;
}

do_install () {
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra
    install -D -m 0644 ${S}/wlan.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/

    #Disbale Runtime PM to fix PCIE AER issue
    sed -i -e 's/^gRuntimePM=1/gRuntimePM=0/g' ${S}/firmware_bin/WCNSS_qcom_cfg.ini

    #Change Default Power Save Offload configuration
    sed -i -e 's/^gEnablePowerSaveOffload=2/gEnablePowerSaveOffload=1/g' ${S}/firmware_bin/WCNSS_qcom_cfg.ini

    install -d ${D}/lib/firmware/wlan/qca_cld
    install -D -m 0644 ${S}/firmware_bin/WCNSS_qcom_cfg.ini ${D}/lib/firmware/wlan/qca_cld/
    install -D -m 0644 ${S}/firmware_bin/WCNSS_qcom_cfg.ini ${D}/lib/firmware/wlan/qcom_cfg.ini
    install -D -m 0644 ${S}/firmware_bin/WCNSS_cfg.dat ${D}/lib/firmware/wlan/qca_cld/
    install -D -m 0644 ${S}/firmware_bin/WCNSS_cfg.dat ${D}/lib/firmware/wlan/cfg.dat

    install -d ${D}/usr/sbin
    install -D -m 0544 ${WORKDIR}/wifi_on.sh ${D}/usr/sbin/
    install -D -m 0544 ${WORKDIR}/wifi_off.sh ${D}/usr/sbin/

    #install systemd service file
    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/init_qti_wlan.service -D ${D}${systemd_unitdir}/system/init_qti_wlan.service
    fi
}

FILES_${PN} = "/usr/sbin/\
               lib/modules/${KERNEL_VERSION}/extra/\
               lib/firmware/\
               etc/init.d/\
               etc/dbus-1/system.d/"

PACKAGES =+ "kernel-module-wlan"
FILES_kernel-module-wlan = "/lib/modules/${KERNEL_VERSION}/extra/wlan.ko"

INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'qcacld-2.0_auto', 'none',d)}"
include ${INCSUFFIX}.inc
