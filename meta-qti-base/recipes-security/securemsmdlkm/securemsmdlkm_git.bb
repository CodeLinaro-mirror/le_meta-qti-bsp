SUMMARY = "QTI securemsm drivers"
DESCRIPTION = "This is the security driver, used to communicate with TrustZone."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/securemsm-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/securemsm-kernel;usehead=1 \
          file://security_load.conf"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/securemsm-kernel"

TECHPACK_MODULE_OUT = "${WORKDIR}/securemsm-kernel-out"
TECHPACK_MODULES = "qseecom_dlkm.ko"

inherit qti-techpack

do_install:append() {
    install -m 0755 ${WORKDIR}/security_load.conf -D ${D}${sysconfdir}/modules-load.d/security_load.conf
}

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
