inherit pkgconfig native autotools qcommon

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=platform/bootable/recovery.git"
#DEPENDS = "libmincrypt-native system-core oem-recovery"
RDEPENDS_${PN} = "zlib bzip2"

PR = "r1"

SRC_URI="${CAF_LA_GIT}/platform/bootable/recovery.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=bootable/recovery/applypatch;subpath=applypatch;"

S = "${WORKDIR}/bootable/recovery/applypatch"

EXTRA_OECONF = " --with-host-os=${HOST_OS}"
