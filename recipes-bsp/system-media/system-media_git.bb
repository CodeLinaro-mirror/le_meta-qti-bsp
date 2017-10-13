inherit autotools pkgconfig qcommon

DESCRIPTION = "system media headers"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "system-core tinyalsa expat"

SRC_URI="${CAF_LA_GIT}/platform/system/media.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=system/media;"

S = "${WORKDIR}/system/media"

EXTRA_OECONF += " --with-glib"

PR = "r2"

FILES_${PN}-dbg    = "${libdir}/.debug/lib*.*"
FILES_${PN}        = "${libdir}/lib*.so.* ${libdir}/pkgconfig/*"
FILES_${PN}-dev    = "${libdir}/lib*.so ${libdir}/lib*.la ${includedir}"
