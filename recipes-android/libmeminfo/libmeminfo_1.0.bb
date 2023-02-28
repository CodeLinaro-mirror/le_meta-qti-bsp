inherit autotools pkgconfig

DESCRIPTION = "Build Android libdmabufinfo for LE"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESPATH =+ "${WORKSPACE}/system/memory:"
SRC_URI   = "file://libmeminfo"

S = "${WORKDIR}/libmeminfo"
DEPENDS += "linux-msm-headers libbase libion libprocinfo gtest libprocinfo"

EXTRA_OECONF_append = " \
    --disable-static \
    --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include \
"

PACKAGES +="${PN}-test-bin"
PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN}     = "${libdir}/pkgconfig/* ${libdir}/* ${sysconfdir}/* ${bindir}/*"
PACKAGE_ARCH = "${MACHINE_ARCH}"

