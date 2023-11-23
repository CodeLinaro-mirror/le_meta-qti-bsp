SUMMARY = "Security Drivers Kernel Modules"
DESCRIPTION = "Security drivers, used to communicate with TrustZone"
HOMEPAGE = "https://codeaurora.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-sec-modules/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-sec-modules;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-sec-modules/security-modules"

TECHPACK_MODULES = "tz_log.ko qtee_shmbridge.ko smcinvoke.ko qcom_scm_oot.ko"
inherit qti-techpack

do_install:append() {
    install -d ${D}${includedir}/linux
    install -m 0755 ${S}/modules-load/tz_log.conf -D ${D}${sysconfdir}/modules-load.d/tz_log.conf
    install -m 0755 ${S}/modules-load/qtee_shmbridge.conf -D ${D}${sysconfdir}/modules-load.d/qtee_shmbridge.conf
    install -m 0755 ${S}/modules-load/smcinvoke.conf -D ${D}${sysconfdir}/modules-load.d/smcinvoke.conf
    install -m 0755 ${S}/modules-load/qcom_scm_oot.conf -D ${D}${sysconfdir}/modules-load.d/qcom_scm_oot.conf
    install -m 0644 ${S}/drivers/smcinvoke.h ${D}${includedir}/linux
}

RPROVIDES:${PN} += "kernel-module-tz-log-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qtee-shmbridge-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-smcinvoke-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcom-scm-oot-${KERNEL_VERSION}"

FILES:${PN} += "${sysconfdir}/modules-load.d/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/*"
