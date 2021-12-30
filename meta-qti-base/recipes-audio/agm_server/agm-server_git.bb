SUMMARY = "AGM Server Library"
DESCRIPTION = "This is the server library of AGM, based on Binder IPC."
HOMEPAGE = "http://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "agm binder glib-2.0 libcutils libutils"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm/plugins/alsalib;subpath=alsalib;usehead=1 \
           file://agm.service \
           "
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/ipc/SwBinders/agm_server"

inherit autotools pkgconfig systemd

SYSTEMD_SERVICE_${PN} = "agm.service"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/agm.service -D ${D}${systemd_unitdir}/system/agm.service
    fi
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
