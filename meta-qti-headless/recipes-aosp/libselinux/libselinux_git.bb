SUMMARY = "Android port of libselinux"
DESCRIPTION = "This directory contains a small port of libselinux for Android. \
It was originally forked in mid-2011, circa libselinux 2.1.0. \
Some changes have been cherry-picked from the upstream libselinux."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://NOTICE;md5=84b4d2c6ef954a2d4081e775a270d0d0"

DEPENDS += "libcutils liblog libmincrypt libpcre"

SRC_URI = "${PATH_TO_REPO}/external/libselinux/.git;protocol=${PROTO};destsuffix=external/libselinux;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/libselinux"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "--with-pcre"

BBCLASSEXTEND = "native"
