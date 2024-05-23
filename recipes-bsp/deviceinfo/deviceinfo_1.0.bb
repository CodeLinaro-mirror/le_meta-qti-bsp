LICENSE          = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"

DESCRIPTION = "Install deviceinfo.xml on target"
PR = "r1"

inherit pkgconfig

FILESPATH =+ "${WORKSPACE}/files:"
SRC_URI = " \
    file://device_info.xml \
"

S = "${WORKDIR}"

do_install_append() {
    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/device_info.xml -D ${D}${sysconfdir}/device_info.xml
}

FILES_${PN} += "${sysconfdir}/"
