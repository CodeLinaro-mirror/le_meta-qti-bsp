SUMMARY = "vidc driver"
DESCRIPTION = "Recipe to generate the vidc driver module. The driver configures the vidc interfaces in the video subsystem."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/video-driver/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/video-driver;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/video-driver"

inherit qti-techpack

TECHPACK_MODULE_OUT = "${WORKDIR}/vendor/qcom/opensource/video-driver-out"
TECHPACK_MODULES = "msm-vidc.ko"
TECHPACK_HEADERS = "1"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
