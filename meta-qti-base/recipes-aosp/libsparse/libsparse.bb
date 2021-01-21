SUMMARY = "Android Sparse library"
DESCRIPTION = "Libparse is a library in common use by the various Android core host applications. \
It provides utilities to convert from raw to sparse images and back."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "zlib"

PR = "r0"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core/libsparse;subpath=libsparse;usehead=1"

S = "${WORKDIR}/system/core/libsparse"

inherit autotools pkgconfig

EXTRA_OECONF_append_class-native = "  --enable-img-convert-utils"

BBCLASSEXTEND = "native"
