inherit autotools pkgconfig

DESCRIPTION = "Build Android libsync"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI = "${CLO_LA_GIT}/platform/system/core/;protocol=${CLO_PROTOCOL};nobranch=1;subpath=libsync;name=libsync"
SRC_URI += "file://configure.ac;subdir=${BPN}"
SRC_URI += "file://Makefile.am;subdir=${BPN}"
SRC_URI += "file://libsync.pc.in;subdir=${BPN}"

SRCREV_libsync="8fbe56b11ee7c1f8c87e9b71d89caa306c6cdebb"
S = "${WORKDIR}/libsync"


DEPENDS += "liblog"
DEPENDS += "glib-2.0"

EXTRA_OECONF = "--with-glib"

PACKAGES =+ "${PN}-test-bin"

FILES_${PN}-test-bin = "${base_bindir}/*"
