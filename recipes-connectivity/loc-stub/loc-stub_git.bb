inherit qcommon

DESCRIPTION = "GPS Loc Stub"
PR = "r1"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/hardware/qcom/gps.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=hardware/qcom/gps/utils/platform_lib_abstractions/loc_stub;subpath=utils/platform_lib_abstractions/loc_stub \
"


S = "${WORKDIR}/hardware/qcom/gps/utils/platform_lib_abstractions/loc_stub"
DEPENDS += "glib-2.0 liblog"
EXTRA_OECONF = "--with-glib"
