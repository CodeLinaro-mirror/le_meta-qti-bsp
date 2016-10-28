DESCRIPTION = "only provide header files reference for camera stack"
SECTION = "camera"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://camera/lib"
SRC_URI  += "file://0001-QCamera2-Add-support-for-64bit-32bit-co-existence.patch"

SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/lib"


do_configure[noexec] = "1"
do_compile[noexec]   = "1"
do_install[noexec]   = "1"

