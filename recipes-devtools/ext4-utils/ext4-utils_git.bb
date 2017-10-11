inherit autotools pkgconfig qcommon

DESCRIPTION = "EXT4 UTILS"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS = "libselinux libsparse libcutils libpcre"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/system/extras.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=ext4_utils;destsuffix=extras/ext4_utils \
"

S = "${WORKDIR}/system/extras/ext4_utils"

#EXTRA_OECONF = " --with-core-includes=${STAGING_INCDIR}"
EXTRA_OECONF = "--with-core-includes=${WORKSPACE}/system/core/include"

CPPFLAGS += "-I${STAGING_INCDIR}/libselinux"
CPPFLAGS += "-I${STAGING_INCDIR}/cutils"
