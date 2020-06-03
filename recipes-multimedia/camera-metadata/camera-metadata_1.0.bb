inherit autotools pkgconfig

DESCRIPTION = "Recipe to provide Camera Metadata library"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESPATH =+ "${WORKSPACE}/frameworks/:"
SRC_URI =+ "file://camera_metadata"

S = "${WORKDIR}/camera_metadata"

DEPENDS += "libcutils"

do_install_append() {
     rm -rf ${D}${includedir}/system/camera_vendor_tags.h
     rm -rf ${D}${includedir}/system/camera_metadata_tags.h
     rm -rf ${D}${includedir}/system/camera_metadata.h
     rm -rf ${D}${includedir}/camera/camera_metadata_hidden.h
     rm -rf ${D}/usr/lib64/libcamera_metadata.so
}

FILES_${PN}-dbg    = "${libdir}/.debug/lib*.*"
FILES_${PN}        = "${libdir}/lib*.so.* ${libdir}/pkgconfig/*"
FILES_${PN}-dev    = "${libdir}/lib*.so ${libdir}/lib*.la ${includedir}"

