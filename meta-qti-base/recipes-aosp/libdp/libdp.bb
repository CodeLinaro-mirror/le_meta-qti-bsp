SUMMARY = "Library for Android dynamic partition "
DESCRIPTION = "Android dynamic partitions are a userspace partitioning system for Android. \
Super image is generated to repace system and vendor image when dynamic partitons are enabled. \
This library is used to parse the meta data in super image and create logic partitions to \
support launch Androind container in Linux"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

SRCREV = "b94377adcbb3377a89edc7939d98ce76ccda1398"
SRC_URI = "\
    git://git.codelinaro.org/clo/la/platform/system/core;protocol=https;nobranch=1; \
    file://0001-libdp-add-support-for-autoconf-build.patch \
    file://0002-libdp-update-fs_mgr-to-work-in-LV.patch \
    file://0001-libdp-liblp-libdm-enable-write-and-test.patch \
    file://0001-liblp-Attribute-partition-has-been-updated.patch \
    file://0001-liblp-Expand-the-metadata-header-for-future-use.patch \
    file://0001-libdp-Create-symlink-for-dynamic-partitions.patch \
"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

src = "if (partition->attributes() & ~(LP_PARTITION_ATTRIBUTE_MASK))"
dst = "        if (partition->attributes() \& LP_PARTITION_ATTR_UPDATED) {\n            static const uint16_t kMinVersion = LP_METADATA_VERSION_FOR_UPDATED_ATTR;\n            metadata->header.minor_version = std::max(metadata->header.minor_version, kMinVersion);\n        }"

do_compile() {
    sed -i "/${src}/{n;n;n;s/$/\n\n${dst}/;}" ${S}/fs_mgr/liblp/builder.cpp
}


DEPENDS += "openssl gtest libsparse ext4-utils"
