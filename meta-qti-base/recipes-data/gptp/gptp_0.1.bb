SUMMARY = "GPTP"
DESCRIPTION = "Time Sensitive Networking stack Time Sync"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "\
    glib-2.0 \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
"

SRC_URI = "\
    ${PATH_TO_REPO}/external/open-avb/.git;protocol=${PROTO};destsuffix=external/open-avb;usehead=1 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/open-avb"

inherit pkgconfig useradd

# Add non-root user vnw for gptp-daemon.service
USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = "--no-create-home --shell /bin/false -g vnw vnw"
GROUPADD_PARAM:${PN} = "net_raw; net_admin; vnw;"

GPTP_AUTO_START_ENABLE = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'NO', 'YES', d)}"
EXTRA_OEMAKE += "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "SYSTEMD_SUPPORT_INCLUDED=1", "SYSTEMD_SUPPORT_INCLUDED=0", d)}"
EXTRA_OEMAKE += "${@oe.utils.conditional('GPTP_AUTO_START_ENABLE', 'YES', 'GPTP_AUTO_START=1', 'GPTP_AUTO_START=0', d)}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        export AVB_FEATURE_GVM_MODE=1
    fi
    oe_runmake gptp
    oe_runmake libgptp
    oe_runmake libgptp_test
}

do_install() {
    install -d ${D}/${bindir}/
    install -d ${D}/${bindir}/gptp/
    install -d ${D}/${libdir}/
    install -d ${D}/${includedir}/
    install -m 0755 ${S}/daemons/gptp/linux/build/obj/daemon_cl ${D}/${bindir}/gptp
    install -m 0755 ${S}/examples/libgptp_test/libgptp_test ${D}/${bindir}/gptp
    install -m 0755 ${S}/lib/libgptp/*.so ${D}/${libdir}
    install -m 0644 ${S}/lib/libgptp/gptp_helper.h ${D}${includedir}

}

PACKAGES =+ "${PN}-test"

RDEPENDS:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"
RDEPENDS:${PN}-test += "${PN}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES:${PN}-test += "${bindir}/gptp/libgptp_test"

