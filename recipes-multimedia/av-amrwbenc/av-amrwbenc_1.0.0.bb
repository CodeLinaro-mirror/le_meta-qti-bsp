SUMMARY = "Codec LIbrary of AMRWBENC from Google Android Project frameworks/av"
SECTION = "multimedia"
HOMEPAGE = "https://source.codeaurora.org/quic/la/platform/frameworks/av/+/master"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://configure.ac;md5=023f900690ca05488f0e28cb4b45af41"

FILESPATH =+ "${WORKSPACE}/audio/mm-audio-opensource:"
SRC_URI = "file://av"
S      = "${WORKDIR}/av"

PR = "r0"
LV = "1.0.0"
LIBV = "1.0"
SRCREV="${AUTOREV}"

PACKAGECONFIG ??= ""
PACKAGES = "${PN}"

inherit autotools gettext pkgconfig

DEPENDS = "glib-2.0"
DEPENDS += "virtual/libc"

EXTRA_OECONF += "--with-glib"
EXTRA_OECONF += "--enable-compile-c"
# EXTRA_OECONF += "--enable-gcc-armv5"
# EXTRA_OECONF += "--enable-gcc-armv7-neon"

FILES_${PN} += "${libdir}/*.so"
FILES_${PN}-dbg += "${libdir}/.debug"
FILES_${PN}-dev += "${libdir}/*.la"
FILES_${PN}-staticdev += "${libdir}/*.a"

#Skips check for .so symlinks
# INSANE_SKIP_${PN} = "dev-so"
INSANE_SKIP_${PN} += "installed-vs-shipped"

do_install() {
     oe_runmake install DESTDIR=${D}

     install -d ${D}${includedir}

     install ${S}/media/libstagefright/codecs/common/include/voAMRWB.h 		${D}${includedir}
     install ${S}/media/libstagefright/codecs/common/include/cmnMemory.h 	${D}${includedir}
     install ${S}/media/libstagefright/codecs/common/include/voAudio.h 		${D}${includedir}
     install ${S}/media/libstagefright/codecs/common/include/voIndex.h 		${D}${includedir}
     install ${S}/media/libstagefright/codecs/common/include/voType.h 		${D}${includedir}
      install ${S}/media/libstagefright/codecs/common/include/voMem.h 		${D}${includedir}
}



