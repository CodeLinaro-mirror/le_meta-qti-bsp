SUMMARY = "Implement the early eth script"
DESCRIPTION = "Implement the early eth script to bring up the eth interface in early time during boot up"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    file://early_eth1.service \
    file://early_eth2.service \
    file://early_eth1_monaco.service \
    file://early_eth1_monaco.sh \
    file://early_eth1.sh \
    file://early_eth2.sh \
"

inherit systemd useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "net_admin"
USERADD_PARAM:${PN} = "--no-create-home -g net_admin --shell /bin/false net_admin"

do_install:append:quin-gvm-lemans() {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/early_eth1.service ${D}${systemd_unitdir}/system/early_eth1.service
    install -m 755 ${WORKDIR}/early_eth2.service ${D}${systemd_unitdir}/system/early_eth2.service
    install -m 0755 ${WORKDIR}/early_eth1.sh ${D}${bindir}/early_eth1.sh
    install -m 0755 ${WORKDIR}/early_eth2.sh ${D}${bindir}/early_eth2.sh
}

SYSTEMD_SERVICE:${PN}:quin-gvm-lemans = "early_eth1.service \
                                        early_eth2.service \
"

do_install:append:quin-gvm-monaco() {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/early_eth1_monaco.sh ${D}${bindir}/early_eth1_monaco.sh
    install -m 0644 ${WORKDIR}/early_eth1_monaco.service ${D}${systemd_unitdir}/system/early_eth1_monaco.service
}

SYSTEMD_SERVICE:${PN}:quin-gvm-monaco = "early_eth1_monaco.service \
"