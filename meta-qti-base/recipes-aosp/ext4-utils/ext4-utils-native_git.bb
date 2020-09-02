inherit native autotools pkgconfig

DESCRIPTION = "EXT4 UTILS"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS = "libselinux-native libsparse-native libcutils-native libpcre-native"

SRC_URI   =  "git://source.codeaurora.org/platform/system/extras.git;protocol=${PROTO};destsuffix=system/extras/ext4_utils;subpath=ext4_utils;nobranch=1"
SRCREV = "8ef7dae5e1a207c4683e7ea3ee534ea94d13787a"


S = "${WORKDIR}/system/extras/ext4_utils"

CPPFLAGS += "-I${STAGING_INCDIR}/libselinux"
