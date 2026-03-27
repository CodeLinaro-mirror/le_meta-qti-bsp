DESCRIPTION = "Use RAM Carveout for journal storage"
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "base"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS = "virtual/kernel"

PR = "r1"
PV = "1.0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/tools/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/tools;usehead=1  \
           file://ramcarveout.service \
           file://setup-ramjournal.service \
           file://setup-ramjournal.sh \
           file://99-ramjournal.conf \
           "
SRCREV = "${AUTOREV}"
SRC_DIR := "${SRC_DIR_ROOT}/vendor/qcom/opensource/tools/"

S = "${WORKDIR}/vendor/qcom/opensource/tools/ramcarveout"

INHIBIT_PACKAGE_STRIP = "1"

SYSTEMD_SERVICE:${PN} = "ramcarveout.service"
SYSTEMD_SERVICE:${PN}:append = " setup-ramjournal.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

inherit module systemd qperf module-sign

FILES:${PN} += "${bindir}/setup-ramjournal.sh"
FILES:${PN} += "${systemd_unitdir}/journald.conf.d/"

# lock to avoid parallel compiling with techpack
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ramcarveout.service ${D}${systemd_unitdir}/system/ramcarveout.service
    install -m 0644 ${WORKDIR}/setup-ramjournal.service ${D}${systemd_unitdir}/system/setup-ramjournal.service

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/setup-ramjournal.sh ${D}${bindir}/setup-ramjournal.sh

    install -d ${D}${systemd_unitdir}/journald.conf.d
    install -m 0644 ${WORKDIR}/99-ramjournal.conf ${D}${systemd_unitdir}/journald.conf.d/99-ramjournal.conf
}
