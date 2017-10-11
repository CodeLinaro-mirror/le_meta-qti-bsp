inherit autotools-brokensep pkgconfig qcommon

DESCRIPTION = "Libselinux"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://NOTICE;md5=84b4d2c6ef954a2d4081e775a270d0d0"

PR = "r0"

DEPENDS = "libpcre libmincrypt liblog libcutils"

SRC_URI = "\
    ${CAF_LA_GIT}/platform/external/libselinux.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=external/libselinux \
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=system/core/include;subpath=include \
"
S = "${WORKDIR}/external/libselinux"

EXTRA_OECONF = " --with-pcre --with-core-includes=${WORKDIR}/system/core/include"
