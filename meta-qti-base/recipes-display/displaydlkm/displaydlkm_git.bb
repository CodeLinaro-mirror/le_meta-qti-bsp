SUMMARY = "Display drivers"
DESCRIPTION = "Build display drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "display-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/display-drivers/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/display-drivers;;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/display-drivers"

inherit qti-techpack

TECHPACK_MODULE_OUT = "${WORKDIR}/display-drivers"
TECHPACK_MODULES = "msm/msm_drm.ko"
TECHPACK_MODULES:quin-gvm-gen4-2 = "msm-hyp/msm_hyp.ko msm-cfg/msm_cfg.ko"
TECHPACK_HEADERS = "1"

do_install:append:sa81x5(){
    install -m 0644 ${S}/config/display_augen3_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

do_install:append:lemans(){
    install -m 0644 ${S}/config/display_augen4_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES_${PN} += "${sysconfdir}/*"
