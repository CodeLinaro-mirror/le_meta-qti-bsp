inherit autotools pkgconfig qcommon

DESCRIPTION = "Android Multimedia Framework"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS = "native-frameworks libhardware system-media"

SRC_URI= "${CAF_LE_GIT}/platform/vendor/qcom-opensource/le-framework.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=frameworks/av;subpath=av"

S = "${WORKDIR}/frameworks/av"

EXTRA_OECONF += " --with-kernel-headers=${STAGING_KERNEL_DIR}/include/uapi"

FILES_${PN}-dbg    = "${libdir}/.debug/libcamera_client.* ${bindir}/.debug/*"
FILES_${PN}        = "${libdir}/libcamera_client.so.* ${libdir}/pkgconfig/* ${bindir}/mtpserver"
FILES_${PN}-dev    = "${libdir}/libcamera_client.so ${libdir}/libcamera_client.la ${includedir}"
