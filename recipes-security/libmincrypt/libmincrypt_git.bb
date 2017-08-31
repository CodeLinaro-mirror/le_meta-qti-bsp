inherit autotools pkgconfig qcommon

DESCRIPTION = "Build Android libmincrypt"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=${BPN};destsuffix=system/core/${BPN} \
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=include;destsuffix=system/core/include \
"

S = "${WORKDIR}/system/core/${BPN}"

EXTRA_OECONF += " --with-core-includes=${WORKDIR}/system/core/include"
