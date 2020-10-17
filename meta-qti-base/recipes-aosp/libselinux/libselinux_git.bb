DESCRIPTION = "Libselinux"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://NOTICE;md5=84b4d2c6ef954a2d4081e775a270d0d0"

DEPENDS = "libpcre libmincrypt liblog libcutils"

SRCREV = "${AUTOREV}"

PR = "r0"

SRC_URI = "${PATH_TO_REPO}/external/libselinux/.git;protocol=${PROTO};destsuffix=external/libselinux;usehead=1"

S = "${WORKDIR}/external/libselinux"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF = " --with-pcre"
