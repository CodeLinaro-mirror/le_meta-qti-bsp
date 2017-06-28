inherit autotools-brokensep pkgconfig
PR = "r0"

DESCRIPTION = "Recovery applypatch"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS = "recovery"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://bootable/recovery/"

S = "${WORKDIR}/bootable/${PN}/"

INSANE_SKIP_${PN} = "installed-vs-shipped"

do_install() {
        install -d ${D}/usr/bin/
	install -m 0744 ${WORKDIR}/../../recovery/git-r7/bootable/recovery/applypatch/applypatch   ${D}/usr/bin/
}
