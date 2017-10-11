inherit autotools-brokensep qcommon pkgconfig

DESCRIPTION = "GPS Utils"
PR = "r1"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/hardware/qcom/gps.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=hardware/qcom/gps/utils;subpath=utils \
"

S = "${WORKDIR}/hardware/qcom/gps/utils"

DEPENDS  += "glib-2.0 loc-pla libcutils liblog"

EXTRA_OECONF = "--with-glib"
