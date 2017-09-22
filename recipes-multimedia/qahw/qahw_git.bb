inherit autotools pkgconfig qcommon

DESCRIPTION = "qahw"
SECTION = "multimedia"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

SRC_URI="${CAF_LA_GIT}/platform/hardware/qcom/audio.git;protocol=git;nobranch=1;subpath=qahw;tag=${CAF_TAG};destsuffix=hardware/qcom/audio/qahw"

S = "${WORKDIR}/hardware/qcom/audio/qahw/"
PR = "r0"

DEPENDS = "libhardware liblog libcutils"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
