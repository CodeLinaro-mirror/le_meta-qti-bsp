SUMMARY = "gps-utils"
DESCRIPTION = "GPS Utils"
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "glib-2.0 libcutils loc-pla-hdr location-api-iface"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps/utils;subpath=utils;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/hardware/qcom/gps/utils"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF = "\
    --with-locpla-includes=${STAGING_INCDIR}/loc-pla \
    --with-glib \
"

CFLAGS:append = " -DUSE_SYSLOG_LOGGING"
CPPFLAGS:append = " -DUSE_SYSLOG_LOGGING"
