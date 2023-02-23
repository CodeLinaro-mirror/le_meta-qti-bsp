SUMMARY = "Libmeminfo utility"
DESCRIPTION = "Build Android libdmabufinfo tools for LV"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS += "virtual/kernel-headers libbase libprocinfo "

SRC_URI = "${PATH_TO_REPO}/src/system/memory/libmeminfo/.git;protocol=${PROTO};destsuffix=src/system/memory/libmeminfo;subpath=libmeminfo;usehead=1 \
           file://0001-libmeminfo-Remove-libion-dependency.patch \
           "
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/src/system/memory/libmeminfo"

inherit autotools pkgconfig

EXTRA_OECONF:append = " \
    --disable-static \
    --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include \
"
