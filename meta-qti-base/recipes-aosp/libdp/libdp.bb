SUMMARY = "Library for Android dynamic partition "
DESCRIPTION = "Android dynamic partitions are a userspace partitioning system for Android. \
Super image is generated to repace system and vendor image when dynamic partitons are enabled. \
This library is used to parse the meta data in super image and create logic partitions to \
support launch Androind container in Linux"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

DEPENDS += "openssl"

SRCREV = "b94377adcbb3377a89edc7939d98ce76ccda1398"
SRC_URI = "\
    git://source.codeaurora.org/quic/la/platform/system/core;protocol=https;nobranch=1; \
    file://0001-libdp-add-support-for-autoconf-build.patch \
    file://0002-libdp-update-fs_mgr-to-work-in-LV.patch \
"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
