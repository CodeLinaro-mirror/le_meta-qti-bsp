SUMMARY = "Library for memory allocator functions for ion"
DESCRIPTION = "Android libion library contains helper functions for using ion. \
Ion is a generalized memory manager introduced in the Android release to address \
the issue of fragmented memory management interfaces across different Android devices."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

DEPENDS += "liblog"

PR = "r1"

SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core/libion;subpath=libion;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core/libion"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "\
    --disable-static \
    --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

do_install_append() {
    install -d ${D}${includedir}/kernel-headers/linux
    install -m 0644 ${S}/kernel-headers/linux/*.h  ${D}${includedir}/kernel-headers/linux
}
