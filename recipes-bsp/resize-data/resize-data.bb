LICENSE          = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"

DESCRIPTION = "Expand userdata to max available partition size"

FILESPATH =+ "${WORKSPACE}/files:"
SRC_URI = "file://resize-data.service"

S = "${WORKDIR}"
PR = "r3"


do_install_append() {
    install -d ${D}${systemd_unitdir}/system/
    install -d ${D}${systemd_unitdir}/system/local-fs.target.wants/

    # install resize-data.service and enable in systemd
    install -m 0644 ${S}/resize-data.service -D ${D}${systemd_unitdir}/system/resize-data.service
    ln -sf ${systemd_unitdir}/system/resize-data.service ${D}${systemd_unitdir}/system/local-fs.target.wants/resize-data.service
}

# Add systemd in package
FILES_${PN} += "${systemd_unitdir}/system/"
