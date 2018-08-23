inherit autotools pkgconfig

DESCRIPTION = "Android IPC utilities"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS = "binder liblog libcutils libsync libhardware libselinux system-core glib-2.0"

do_configure_prepend(){
    if [ ! -d "${STAGING_DIR_TARGET}/usr/lib" ]; then
        mkdir ${STAGING_DIR_TARGET}/usr/lib
        cp ${STAGING_LIBDIR}/*.o ${STAGING_DIR_TARGET}/usr/lib
    fi
}

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://frameworks/libui"


S = "${WORKDIR}/frameworks/libui"

EXTRA_OECONF += " --with-core-includes=${WORKSPACE}/system/core/include --with-glib"

CFLAGS += "-I${STAGING_INCDIR}/libselinux"
CPPFLAGS += " -I${STAGING_INCDIR}/c++/6.4.0"
CPPFLAGS += " -I${STAGING_INCDIR}/c++/6.4.0/aarch64-oe-linux"
CPPFLAGS += " -I${STAGING_INCDIR}/c++/6.4.0/arm-oemllib32-linux-gnueabi"
CPPFLAGS += " -I${WORKSPACE}/system/core/libsync/include"
CPPFLAGS += " -I${WORKSPACE}/system/core/libsync/include/sync"


CPPFLAGS += "-fpermissive"
LDFLAGS  += "-llog"

FILES_${PN}-libui-dbg    = "${libdir}/.debug/libui.*"
FILES_${PN}-libui        = "${libdir}/libui.so.*"
FILES_${PN}-libbui-dev    = "${libdir}/libui.so ${libdir}/libui.la ${includedir}"
