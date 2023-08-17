SUMMARY = "safelinux system configuration"
DESCRIPTION = "Add default system configuration"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-system-cfg;usehead=1 \
    file://eth0.network \
    file://vm_net.conf \
"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg"

do_compile[noexec] = "1"

do_install:append() {
    install -m 0755 ${S}/modules-autoload-config/i2cdev.conf -D ${D}${libdir}/modules-load.d/i2cdev.conf
    install -m 0755 ${WORKDIR}/vm_net.conf -D ${D}${libdir}/modules-load.d/vm_net.conf

    install -d ${D}${sysconfdir}/systemd/network/
    install -m 0644 ${WORKDIR}/eth0.network ${D}${sysconfdir}/systemd/network/eth0.network
}

FILES:${PN} += "${libdir}/modules-load.d/*"
FILES:${PN} += "${sysconfdir}/*"
