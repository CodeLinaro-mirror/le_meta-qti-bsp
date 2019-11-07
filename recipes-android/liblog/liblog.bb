inherit autotools-brokensep pkgconfig

DESCRIPTION = "Build Android liblog"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

FILESPATH =+ "${WORKSPACE}/system/core/:"
SRC_URI   = "file://liblog"
SRC_URI  += "file://50-log.rules"

S = "${WORKDIR}/liblog"

DEPENDS += " glib-2.0"

BBCLASSEXTEND = "native"

EXTRA_OECONF += " --with-core-includes=${WORKSPACE}/system/core/include"
EXTRA_OECONF += " --disable-static"
EXTRA_OECONF += " --with-glib"

do_install_append() {
    if [ "${CLASSOVERRIDE}" = "class-target" ]; then
       install -m 0644 -D ../50-log.rules ${D}${sysconfdir}/udev/rules.d/50-log.rules
    fi
}
