DESCRIPTION = "sec-config file for sensors"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://sec_config"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -m 0444 ${WORKDIR}/sec_config -D ${D}${sysconfdir}/sec_config
}
