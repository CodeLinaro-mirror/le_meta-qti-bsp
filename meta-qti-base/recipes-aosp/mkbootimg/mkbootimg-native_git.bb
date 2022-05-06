DESCRIPTION = "Boot image creation tool from Android"
HOMEPAGE = "http://android.git.kernel.org/?p=platform/system/core.git"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libmincrypt-native glib-2.0-native"

PROVIDES = "mkbootimg-native"

PR = "r6"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core/mkbootimg;subpath=mkbootimg;usehead=1"
SRC_URI:append = " file://makefile;subdir=system/core/mkbootimg"

S = "${WORKDIR}/system/core/mkbootimg"

inherit native

CFLAGS += " -Dstrlcpy=g_strlcpy "
EXTRA_OEMAKE = "INCLUDES='-Imincrypt' LIBS='-lmincrypt -lglib-2.0'"

do_configure[noexec]="1"

do_install() {
        install -d ${D}${bindir}
        install ${S}/mkbootimg ${D}${bindir}
}

NATIVE_INSTALL_WORKS = "1"
