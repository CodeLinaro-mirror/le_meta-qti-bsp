SUMMARY = "Tool for symlink firmware image"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://firmware-links.c \
           file://firmware-links.service \
           file://bt-firmware-links.service"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE_${PN} = "firmware-links.service bt-firmware-links.service"

SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} -o ${S}/firmware-links ${S}/firmware-links.c
}

do_install() {
        install -d ${D}${base_sbindir}
        install -m 0755 ${S}/firmware-links ${D}${base_sbindir}/firmware-links
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/firmware-links.service ${D}${systemd_unitdir}/system/firmware-links.service
        install -m 0644 ${S}/bt-firmware-links.service ${D}${systemd_unitdir}/system/bt-firmware-links.service
}

FILES_${PN} += " ${base_sbindir}/firmware-links \
                 ${systemd_unitdir}/system/firmware-links.service \
                 ${systemd_unitdir}/system/bt-firmware-links.service"
