inherit update-rc.d qcommon

DESCRIPTION = "WCNSS platform"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
LICENSE = "BSD"

PR = "r1"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/vendor/qcom-opensource/wlan/prima.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=firmware_bin;destsuffix=qcom-opensource/wlan/firmware_bin \
    ${CAF_LA_GIT}/platform/vendor/qcom/ferrum.git;protocol=git;tag=${CAF_TAG};nobranch=1;destsuffix=android_compat/device/qcom/msm8909 \
"
SRC_URI += "file://set_wcnss_mode"
SRC_URI += "file://wcnss_wlan.service"

S = "${WORKDIR}/qcom-opensource/wlan/firmware_bin"

do_install[cleandirs] += "${TMPDIR}/work-shared/${MACHINE}/wcnss-bins"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/init.d
    install "${WORKDIR}"/set_wcnss_mode ${D}/etc/init.d

	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -d ${D}/etc/systemd/system/
		install -m 0644 ${WORKDIR}/wcnss_wlan.service -D ${D}/etc/systemd/system/wcnss_wlan.service
		install -d ${D}/etc/systemd/system/multi-user.target.wants/
		# enable the service for multi-user.target
		ln -sf /etc/systemd/wcnss_wlan.service \
		${D}/etc/systemd/system/multi-user.target.wants/wcnss_wlan.service
	fi

    mkdir -p ${D}/lib/firmware/wlan/prima
    cp -pP ${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_qcom_cfg.ini ${D}/lib/firmware/wlan/prima

    # Copy WCNSS bins into work-shared dir to include in /persist during image creation.
    if [ -e "${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_qcom_wlan_nv.bin" ];then
        cp -pP ${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_qcom_wlan_nv.bin ${TMPDIR}/work-shared/${MACHINE}/wcnss-bins
    fi

    if [ -e "${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_wlan_dictionary.dat" ]; then
        cp -pP ${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_wlan_dictionary.dat ${TMPDIR}/work-shared/${MACHINE}/wcnss-bins
    elif [ -e "${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}_32/WCNSS_wlan_dictionary.dat" ]; then
        cp -pP ${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}_32/WCNSS_wlan_dictionary.dat ${TMPDIR}/work-shared/${MACHINE}/wcnss-bins
    fi

    # Create symlinks to /persist path. Actual files will be placed in /persist during image creation.
    install -d ${D}/lib/firmware/wlan/prima
    ln -s /persist/WCNSS_qcom_wlan_nv.bin ${D}/lib/firmware/wlan/prima/WCNSS_qcom_wlan_nv.bin
    ln -s /persist/WCNSS_wlan_dictionary.dat ${D}/lib/firmware/wlan/prima/WCNSS_wlan_dictionary.dat
}

INITSCRIPT_NAME = "set_wcnss_mode"
INITSCRIPT_PARAMS = "start 60 2 3 4 5 . stop 20 0 1 6 ."

FILES_${PN} = "/lib/firmware/*"
FILES_${PN} += "/etc/*"
FILES_${PN} += "/lib/firmware/wlan/prima/*"

