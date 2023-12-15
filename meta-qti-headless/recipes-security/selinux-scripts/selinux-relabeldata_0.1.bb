SUMMARY = "SELinux init script"
DESCRIPTION = "Relabel SELinux contexts for /data."
HOMEPAGE = "http://git.codelinaro.org"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

SYSTEMD_SERVICE:${PN} = "${PN}.service"

SRC_URI = "file://${BPN}.sh \
        file://${BPN}.service \
"

inherit systemd

RDEPENDS:${PN} = "\
    coreutils \
    libselinux-bin \
    policycoreutils-setfiles \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/${BPN}.service ${D}${systemd_unitdir}/system
        install -d ${D}${bindir}
        install -m 0755 ${WORKDIR}/${BPN}.sh ${D}${bindir}
    fi
}
