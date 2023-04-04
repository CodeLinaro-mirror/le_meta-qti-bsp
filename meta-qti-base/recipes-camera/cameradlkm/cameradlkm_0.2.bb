SUMMARY = "Camera drivers"
DESCRIPTION = "Build QCX camera drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/qcx-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/qcx-kernel;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/qcx-kernel"

inherit qti-techpack

TECHPACK_MODULE_OUT = "${WORKDIR}/qcx-kernel"
TECHPACK_MODULES = "camera.ko"
TECHPACK_HEADERS = "1"
TECHPACK_MAKE_ARGS = "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', "${EXTRA_OEMAKE} QTI_TECHPACK=true", "", d)}"

do_install:append() {
    install -m 0755 ${S}/config/camera_augen4_load.conf -D ${D}${sysconfdir}/modules-load.d/camera_load.conf
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES_${PN} += "${sysconfdir}/*"


