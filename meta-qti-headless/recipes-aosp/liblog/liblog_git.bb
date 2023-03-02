SUMMARY = "liblog - Android NDK logger interfaces"
DESCRIPTION = "liblog  represents  an interface to the volatile Android Logging system \
for NDK (Native) applications  and  libraries.  Interfaces  for  either writing  or reading logs."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=2754aa537525e0fed3d297e8379b6d81"

DEPENDS += "glib-2.0"

PR = "r1"

SRC_URI = "\
    ${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core/liblog"

inherit autotools-brokensep pkgconfig

CFLAGS += "-Dstrlcpy=g_strlcpy "
LDFLAGS += "-lglib-2.0 "

EXTRA_OECONF += "\
    --with-core-includes=${WORKDIR}/system/core/include \
    --disable-static \
"

BBCLASSEXTEND = "native"
