SUMMARY = "Libmeminfo utility"
DESCRIPTION = "Build Android libdmabufinfo tools for LV"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS += "virtual/kernel-headers libbase libprocinfo "

SRC_URI = "${CLO_LE_GIT}/platform/system/memory/libmeminfo;protocol=https;branch=memory-le-apps.lnx.1.0;destsuffix=src/system/memory/libmeminfo \
           file://0001-libmeminfo-Remove-libion-dependency.patch"

SRCREV = "e5690f41db4637600d40b74d7eb8207837edfb85"

S = "${WORKDIR}/src/system/memory/libmeminfo"

inherit autotools pkgconfig

EXTRA_OECONF:append = " \
    --disable-static \
    --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include \
"
