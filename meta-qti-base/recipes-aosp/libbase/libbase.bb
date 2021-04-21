SUMMARY = "Android base library"
DESCRIPTION = "This library provides APIs for basic tasks like \
handling files, Unicode strings, logging, memory allocation, \
integer parsing, etc."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

DEPENDS += "libcutils libselinux"

PR = "r1"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"

S = "${WORKDIR}/system/core/base"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-core-sourcedir=${WORKDIR}/system/core"

BBCLASSEXTEND = "native"
