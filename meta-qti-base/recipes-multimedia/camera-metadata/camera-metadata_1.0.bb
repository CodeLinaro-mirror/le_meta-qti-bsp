inherit autotools pkgconfig

DESCRIPTION = "Recipe to provide Camera Metadata library"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://source.codeaurora.org/quic/le/platform/vendor/qcom-opensource/le-framework.git;protocol=${PROTO};destsuffix=frameworks/camera_metadata;subpath=camera_metadata;nobranch=1"  
SRCREV = "512dafe851af504ac4642acbd25936aa232711a4"  

S = "${WORKDIR}/frameworks/camera_metadata"

DEPENDS += "libcutils"

FILES_${PN}-dbg    = "${libdir}/.debug/lib*.*"
FILES_${PN}        = "${libdir}/lib*.so.* ${libdir}/pkgconfig/*"
FILES_${PN}-dev    = "${libdir}/lib*.so ${libdir}/lib*.la ${includedir}"

