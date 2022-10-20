inherit autotools pkgconfig

DESCRIPTION = "Libraries for Android dynamic partition "
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "${CLO_LA_GIT}/platform/system/core;protocol=https;nobranch=1;name=core \
           file://0001-libdp-add-support-for-autoconf-build.patch \
           file://0001-libdp-Android11-ota-lib-porting-libbase_r.patch \
           file://0002-libdp-Android11-ota-lib-porting-liblp.patch \
           file://0003-libdp-Android11-ota-lib-porting-libdm.patch \
           file://0004-libdp-Android11-ota-lib-porting-fs_mgr.patch \
           file://0001-libdp-Create-symlink-for-dynamic-partitions.patch \
           file://0001-libdp-libdm-add-uuid-link-for-partions.patch \
           file://0001-libdp-update-fs_mgr-to-work-in-LV.patch \
           "
SRCREV_core = "f9a075a9078eaebee234fb9be2f043613fe63da8"

S = "${WORKDIR}/git"

DEPENDS += "openssl gtest libsparse ext4-utils"

PACKAGES =+ "${PN}-test"
FILES_${PN}-test += "${bindir}/lp_test \
                     ${bindir}/dm_test \
                    "
