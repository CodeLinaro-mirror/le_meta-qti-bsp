DESCRIPTION = "EXT4 UTILS"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libselinux-native libsparse-native libcutils-native libpcre-native"

SRCREV = "${AUTOREV}"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/system/extras/.git;protocol=${PROTO};destsuffix=system/extras/ext4_utils;subpath=ext4_utils;usehead=1"

S = "${WORKDIR}/system/extras/ext4_utils"

inherit native autotools pkgconfig

CPPFLAGS += "-I${STAGING_INCDIR}/libselinux"
