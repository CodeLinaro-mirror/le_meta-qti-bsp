inherit native qcommon

PR = "r4"

DESCRIPTION = "fs_config tool from Android"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "http://android.git.kernel.org/?p=platform/system/core.git"

SRC_URI = "${CAF_LA_GIT}/platform/build.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=android_compat/build/tools/fs_config;subpath=tools/fs_config"

S = "${WORKDIR}/android_compat/build/tools/fs_config/"

EXTRA_OECONF = "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                --with-core-headers=${STAGING_INCDIR_NATIVE}"

BBCLASSEXTEND = "native"
