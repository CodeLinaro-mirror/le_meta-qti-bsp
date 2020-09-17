inherit native autotools pkgconfig

DESCRIPTION = "DTBO image creation tool from Android"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "http://developer.android.com/"
PROVIDES = "mkdtimg-native"
#DTC provide the libfdt.h
DEPENDS += " dtc-native"

PR = "r1"
SRC_URI = "${PATH_TO_REPO}/system/libufdt.git;protocol=${PROTO};destsuffix=system/libufdt;nobranch=1"
S = "${WORKDIR}/system/libufdt"
SRCREV = "f83eb5a3630ffd5842949451c4fe0c235d32defa"

# let libufdt suport autoconf
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append = " file://0001-libufdt-support-autoconf-compile.patch"
