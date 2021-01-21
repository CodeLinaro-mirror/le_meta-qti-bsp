SUMMARY = "Android ext4-utils tools"
DESCRIPTION = "Command line tools to make sparse images from ext4 file system images \
and android images(.img) with ext4 file systems. This package contains tools like mkuserimg, ext4fixup and make_ext4fs tools."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "libcutils libpcre libselinux libsparse"

SRCREV = "${AUTOREV}"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/system/extras/.git;protocol=${PROTO};destsuffix=system/extras/ext4_utils;subpath=ext4_utils;usehead=1"

S = "${WORKDIR}/system/extras/ext4_utils"

inherit autotools pkgconfig

CPPFLAGS += "-I${STAGING_INCDIR}/libselinux"
CPPFLAGS += "-I${STAGING_INCDIR}/cutils"
