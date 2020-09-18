inherit autotools-brokensep

DESCRIPTION = "Safe integer operation library for C"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

SRC_URI   =  "git://source.codeaurora.org/platform/external/safe-iop.git;protocol=https;destsuffix=external/safe-iop;nobranch=1"
SRC_URI_append  = " file://autotools.patch"
SRCREV = "aa0725fb1da35e47676b6da30009322eb5ed59be"

S = "${WORKDIR}/external/safe-iop"

PR = "r0"
