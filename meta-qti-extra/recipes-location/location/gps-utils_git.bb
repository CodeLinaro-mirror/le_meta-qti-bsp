inherit autotools-brokensep qcommon pkgconfig

DESCRIPTION = "GPS Utils"
PR = "r1"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "git://source.codeaurora.org/quic/le/platform/hardware/qcom/gps.git;protocol=https;destsuffix=hardware/qcom/gps/utils;subpath=utils;nobranch=1"
SRCREV = "ea4414714bf747dec063b5ebc63e07d3620f3e2c"
S = "${WORKDIR}/hardware/qcom/gps/utils"

DEPENDS = "glib-2.0 libcutils loc-pla-hdr location-api-iface"
EXTRA_OECONF = "--with-locationapi-includes=${STAGING_INCDIR}/location-api-iface \
                --with-locpla-includes=${STAGING_INCDIR}/loc-pla \
                --with-glib"
