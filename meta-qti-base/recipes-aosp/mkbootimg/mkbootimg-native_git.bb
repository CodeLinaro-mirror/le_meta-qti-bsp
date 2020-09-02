inherit native

DESCRIPTION = "Boot image creation tool from Android"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "http://android.git.kernel.org/?p=platform/system/core.git"
PROVIDES = "mkbootimg-native"

S = "${WORKDIR}/system/core/mkbootimg"
DEPENDS = "libmincrypt-native glib-2.0-native"

SRC_URI  =  "git://source.codeaurora.org/quic/le/platform/system/core.git;protocol=${PROTO};destsuffix=system/core/mkbootimg;subpath=mkbootimg;nobranch=1"
SRC_URI_append = " file://makefile;subdir=system/core/mkbootimg"

SRCREV = "a108d342592e6d03560729e589ba1ac6f7eaa440"
PR = "r6"

CFLAGS += " -Dstrlcpy=g_strlcpy "
EXTRA_OEMAKE = "INCLUDES='-Imincrypt' LIBS='-lmincrypt -lglib-2.0'"

do_configure[noexec]="1"
do_install() {
	install -d ${D}${bindir}
	install ${S}/mkbootimg ${D}${bindir}
}

NATIVE_INSTALL_WORKS = "1"
