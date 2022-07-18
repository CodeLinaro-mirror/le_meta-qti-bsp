SUMMARY = "Open AVB"
DESCRIPTION = "Open Source Project for Audio Video Bridging/Time Sensitive Networking stack"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "\
    alsa-lib cmake-native glib-2.0 gstreamer1.0 gstreamer1.0-plugins-base libpcap \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
    pciutils \
"

SRC_URI = "\
    ${PATH_TO_REPO}/external/open-avb/.git;protocol=${PROTO};destsuffix=external/open-avb;usehead=1 \
    file://gptp-daemon.service \
    file://gptp-daemon-tmpfilesd.conf \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/open-avb"

inherit systemd pkgconfig

GPTP_AUTO_START_ENABLE = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'NO', 'YES', d)}"
EXTRA_OEMAKE += "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "SYSTEMD_SUPPORT_INCLUDED=1", "SYSTEMD_SUPPORT_INCLUDED=0", d)}"
EXTRA_OEMAKE += "${@oe.utils.conditional('GPTP_AUTO_START_ENABLE', 'YES', 'GPTP_AUTO_START=1', 'GPTP_AUTO_START=0', d)}"
SYSTEMD_SERVICE:${PN} = "${@oe.utils.conditional('GPTP_AUTO_START_ENABLE', 'YES', 'gptp-daemon.service', '', d)}"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
PACKAGE_ARCH = "${MACHINE_ARCH}"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        export AVB_FEATURE_GVM_MODE=1
    fi

    export AVB_FEATURE_NEUTRINO=1
    export AVB_FEATURE_INTF_ALSA2=0
    export AVB_FEATURE_GSTREAMER=1
    export GSTREAMER_1_0=1

    echo ${FILESEXTRAPATHS}
    echo ${subdir}

    mkdir -p ${S}/daemons/maap/build
    oe_runmake daemons_all
    make avtp_pipeline

    oe_runmake libgptp
    oe_runmake libgptp_test
}

do_install() {
    install -d ${D}/${bindir}/
    install -d ${D}/${bindir}/avb/
    install -d ${D}/${libdir}/
    install -d ${D}/${includedir}/
    install -m 0755 ${S}/daemons/maap/linux/maap_daemon ${D}/${bindir}/avb
    install -m 0755 ${S}/daemons/mrpd/mrpd ${D}/${bindir}/avb
    install -m 0755 ${S}/daemons/mrpd/mrpctl ${D}/${bindir}/avb
    install -m 0755 ${S}/daemons/gptp/linux/build/obj/daemon_cl ${D}/${bindir}/avb
    install -m 0755 ${S}/lib/avtp_pipeline/build/bin/* ${D}/${bindir}/avb
    install -m 0755 ${S}/lib/avtp_pipeline/build/lib/*.so ${D}/${libdir}
    install -m 0755 ${S}/examples/libgptp_test/libgptp_test ${D}/${bindir}/avb
    install -m 0755 ${S}/lib/libgptp/*.so ${D}/${libdir}
    install -m 0644 ${S}/lib/libgptp/gptp_helper.h ${D}${includedir}

    if (test "x${GPTP_AUTO_START_ENABLE}" == "xYES"); then
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
            install -d ${D}${systemd_unitdir}/system/
            install -m 0644 ${WORKDIR}/gptp-daemon.service -D ${D}${systemd_unitdir}/system/gptp-daemon.service
            install -d ${D}${sysconfdir}/tmpfiles.d/
            install -m 0644 ${WORKDIR}/gptp-daemon-tmpfilesd.conf ${D}${sysconfdir}/tmpfiles.d/gptp-daemon-tmpfilesd.conf
        fi
    fi
}

PACKAGES =+ "libgptp libgptp-dev libgptp-test"

RDEPENDS:libgptp += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"
RDEPENDS:libgptp-test += "libgptp"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES:libgptp += "${libdir}/libgptp.so"
FILES:libgptp-dev += "${includedir}/gptp_helper.h"
FILES:libgptp-test += "${bindir}/avb/libgptp_test"

