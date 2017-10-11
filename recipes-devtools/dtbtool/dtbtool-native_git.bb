inherit native qcommon

DESCRIPTION = "Boot image creation tool from Android"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r4"

SRC_URI = " \
    ${CAF_LA_GIT}/device/qcom/common.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=android_compat/device/qcom/common/${BPN};subpath=${BPN} \
"
SRC_URI  += "file://makefile"

S = "${WORKDIR}/android_compat/device/qcom/common/${BPN}"

do_patch_append () {
    bb.build.exec_func('do_copy_make', d)
}

do_copy_make () {
    cp -f ${WORKDIR}/makefile ${S}
}

do_install() {
    install -d ${D}${bindir}
    install ${BPN} ${D}${bindir}
}