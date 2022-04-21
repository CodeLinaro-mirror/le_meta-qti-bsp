inherit autotools pkgconfig

DESCRIPTION = "Libraries for Android dynamic partition "
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "${CLO_LA_GIT}/platform/system/core;protocol=https;nobranch=1;name=core \
           file://0001-libdp-add-support-for-autoconf-build.patch \
           file://0002-libdp-update-fs_mgr-to-work-in-LV.patch \
           file://0001-liblp-Expand-the-metadata-header-for-future-use.patch \
           file://0001-libdp-Create-symlink-for-dynamic-partitions.patch \
           "
SRCREV_core = "b94377adcbb3377a89edc7939d98ce76ccda1398"

S = "${WORKDIR}/git"

DEPENDS += "openssl"
