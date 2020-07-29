SUMMARY = "System Media Library"
DESCRIPTION = "This it the system media library, used by audiohal to control sound card, set mixer path,etc."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS += "expat glib-2.0 system-core tinyalsa"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks/system/media;subpath=system/media;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/frameworks/system/media"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-glib"
do_install_append() {
    install -m 0644 ${S}/audio/include/system/*.h ${D}${includedir}/system
}
