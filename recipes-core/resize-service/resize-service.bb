SUMMARY = "Tool for resize data partition"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://resize-userdata.service"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE_${PN} = "resize-userdata.service"

SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/resize-userdata.service ${D}${systemd_unitdir}/system/resize-userdata.service
}

FILES_${PN} += "${systemd_unitdir}/system/resize-userdata.service"

RDEPENDS_${PN} += "e2fsprogs-resize2fs"
