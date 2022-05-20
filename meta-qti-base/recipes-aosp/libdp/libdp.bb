inherit autotools pkgconfig

DESCRIPTION = "Libraries for Android dynamic partition "
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "${CLO_LA_GIT}/platform/system/core;protocol=https;nobranch=1;name=core \
           file://0001-libdp-add-support-for-autoconf-build.patch \
           file://0002-libdp-update-fs_mgr-to-work-in-LV.patch \
           file://0001-libdp-liblp-libdm-enable-write-and-test.patch \
           file://0001-liblp-Attribute-partition-has-been-updated.patch \
           file://0001-liblp-Expand-the-metadata-header-for-future-use.patch \
           file://0001-libdp-Create-symlink-for-dynamic-partitions.patch \
           "
SRCREV_core = "b94377adcbb3377a89edc7939d98ce76ccda1398"

S = "${WORKDIR}/git"

src = "if (partition->attributes() & ~(LP_PARTITION_ATTRIBUTE_MASK))"
dst = "        if (partition->attributes() \& LP_PARTITION_ATTR_UPDATED) {\n            static const uint16_t kMinVersion = LP_METADATA_VERSION_FOR_UPDATED_ATTR;\n            metadata->header.minor_version = std::max(metadata->header.minor_version, kMinVersion);\n        }"

do_compile() {
    sed -i "/${src}/{n;n;n;s/$/\n\n${dst}/;}" ${S}/fs_mgr/liblp/builder.cpp
}


DEPENDS += "openssl gtest libsparse ext4-utils"
