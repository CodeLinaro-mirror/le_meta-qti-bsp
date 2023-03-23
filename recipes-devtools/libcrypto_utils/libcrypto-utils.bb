inherit autotools pkgconfig

DESCRIPTION = "Build Android crypto utils"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"

SRC_URI = "${GIT_LA_URI}/platform/system/core/;protocol=${PROTOCOL};nobranch=1;subpath=libcrypto_utils;rev=fb09a4583339baba8eff9ec52f30710572c9632c"
SRC_URI += "file://Add-autotool-make-files-for-libcrypto_utils.patch"

S = "${WORKDIR}/libcrypto_utils"

EXTRA_OECONF_class-native = "--with-header-includes=${S}/include"

DEPENDS += "openssl"

BBCLASSEXTEND += "native"
