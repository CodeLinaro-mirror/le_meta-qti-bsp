DESCRIPTION = "Build Android libmincrypt"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"

S = "${WORKDIR}/system/core/libmincrypt"

inherit autotools pkgconfig

EXTRA_OECONF = " --with-core-includes=${WORKDIR}/system/core/include"

BBCLASSEXTEND = "native"
