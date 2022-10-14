SUMMARY = "Camera drivers"
DESCRIPTION = "Build camera drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "camera-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/ais-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/ais-kernel;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/ais-kernel"

inherit qti-techpack

TECHPACK_MODULE_OUT = "${WORKDIR}/ais-kernel"
TECHPACK_MODULES = "ais.ko"
TECHPACK_HEADERS = "1"

do_install:append() {
    install -m 0755 ${S}/config/camera_augen3_load.conf -D ${D}${sysconfdir}/modules-load.d/camera_load.conf
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES_${PN} += "${sysconfdir}/*"
