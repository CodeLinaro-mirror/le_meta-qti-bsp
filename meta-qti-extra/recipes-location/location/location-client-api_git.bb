SUMMARY = "location-client-api"
DESCRIPTION = "location client api library"
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "glib-2.0 gps-utils libcutils loc-pla-hdr location-api-msg-proto"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/client_api;subpath=client_api;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/qcom-opensource/location/client_api"

inherit autotools-brokensep pkgconfig

#Enable syslogging
CFLAGS_append = " -DUSE_SYSLOG_LOGGING"
CPPFLAGS_append = " -DUSE_SYSLOG_LOGGING"

EXTRA_OECONF = "--with-glib"
