inherit qcommon

DESCRIPTION = "Safe integer operation library for C"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

SRC_URI="${CAF_LA_GIT}/platform/external/safe-iop.git;tag=${CAF_TAG};protocol=git;nobranch=1;destsuffix=external/safe-iop"
SRC_URI  += "file://autotools.patch"

S = "${WORKDIR}/external/safe-iop"

PR = "r0"
