DESCRIPTION = "Build Android liblog"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0"

PR = "r1"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
SRC_URI_append = " file://50-log.rules"

S = "${WORKDIR}/system/core/liblog"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += " --with-core-includes=${WORKDIR}/system/core/include"
EXTRA_OECONF += " --disable-static"

do_install_append_class-target() {
    install -m 0644 -D ${WORKDIR}/50-log.rules ${D}${sysconfdir}/udev/rules.d/50-log.rules
}

BBCLASSEXTEND = "native"

CFLAGS += " -Dstrlcpy=g_strlcpy "
LDFLAGS += " -lglib-2.0 "
