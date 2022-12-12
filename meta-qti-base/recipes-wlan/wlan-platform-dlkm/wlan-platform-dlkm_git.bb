SUMMARY = "Wlan platform drivers"
DESCRIPTION = "Build wlan platform drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "wlan-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/wlan/platform/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/wlan/platform;;usehead=1"

S = "${WORKDIR}/vendor/qcom/opensource/wlan/platform"

SRCREV = "${AUTOREV}"

TECHPACK_MODULE_OUT = "${WORKDIR}/wlan-platform-dlkm"
TECHPACK_MODULES = "cnss2/cnss2.ko"
TECHPACK_MODULES += "cnss_utils/cnss_utils.ko"
TECHPACK_MODULES += "cnss_utils/wlan_firmware_service.ko"
TECHPACK_MODULES += "cnss_utils/cnss_plat_ipc_qmi_svc.ko"
TECHPACK_MODULES += "cnss_genl/cnss_nl.ko"

inherit qti-techpack

do_install:append(){
    dlkmdir=${D}${includedir}/wlan-platform
    install -d ${dlkmdir}
    install -d ${dlkmdir}/inc
    install -m 0644 ${TECHPACK_MODULE_OUT}/Module.symvers ${dlkmdir}/
    install -m 0644 ${S}/inc/* ${dlkmdir}/inc/
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${includedir}/wlan-platform/*"
