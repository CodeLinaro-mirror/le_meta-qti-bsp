inherit pkgconfig autotools multilib_header

DESCRIPTION = "dump and restore utility to create partition backup and restore it"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"

SRC_URI = "git://git.code.sf.net/p/dump/code.git;branch=main"

# This recipe using tag v0.4b47 with mentioned SRCREV
SRCREV = "cb48d59c9e2a9997ac82e1eef117614b2261d737"

S = "${WORKDIR}/git"

# Specify any options you want to pass to the configure script using EXTRA_OECONF:
EXTRA_OECONF = ""

# Add pkg-config dependencies : ext2fs termcap tinfo ncurses lzo2 readline bz2 sqlite3 uuid libselinux zlib blkid openssl
DEPENDS = "util-linux e2fsprogs readline libselinux zlib lzo bzip2 sqlite ncurses openssl"


