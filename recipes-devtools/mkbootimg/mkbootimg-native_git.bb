inherit native qcommon

DESCRIPTION = "Boot image creation tool from Android"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "libmincrypt-native"

PR = "r6"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=${BPN};destsuffix=system/core/${BPN} \
"
SRC_URI  += "file://makefile"

S = "${WORKDIR}/system/core/${BPN}"

do_patch_append () {
    bb.build.exec_func('do_copy_make', d)
}

do_copy_make () {
    cp -f ${WORKDIR}/makefile ${S}
}

EXTRA_OEMAKE += "INCLUDES='-Imincrypt' LIBS='${libdir}/libmincrypt.a'"

do_install () {
       install -d ${D}${bindir}
       install ${BPN} ${D}${bindir}
}
