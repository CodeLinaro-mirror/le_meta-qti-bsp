inherit autotools-brokensep pkgconfig

DESCRIPTION = "Libselinux"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://NOTICE;md5=84b4d2c6ef954a2d4081e775a270d0d0"

PR = "r0"

DEPENDS = "libpcre libmincrypt liblog libcutils"

SRC_URI = "git://source.codeaurora.org/platform/external/libselinux.git;protocol=${PROTO};destsuffix=external/libselinux;nobranch=1"
S = "${WORKDIR}/external/libselinux"

SRCREV = "ee539d24828440eda6115da4f300937383c6a98d"

EXTRA_OECONF = " --with-pcre"
