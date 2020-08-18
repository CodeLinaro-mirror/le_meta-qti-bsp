SUMMARY = "Dynamic partition loader"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS_prepend := "${THISDIR}:"

SRC_URI = "file://dploader"
S = "${WORKDIR}/dploader"

inherit autotools pkgconfig systemd

SYSTEMD_SERVICE_${PN} = "dploader.service"

DEPENDS += "libdp"
RDEPENDS_${PN} += "libdp"

do_install_append() {
       install -d ${D}${systemd_unitdir}/system/
       install -m 0644 ${WORKDIR}/dploader/dploader.service ${D}${systemd_unitdir}/system/dploader.service
}
