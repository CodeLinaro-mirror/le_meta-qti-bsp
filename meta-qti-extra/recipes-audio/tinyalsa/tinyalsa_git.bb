SUMMARY = "Tinyalsa Library"
DESCRIPTION = "This is tiny library to interface with ALSA."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "libcutils"

SRC_URI = "\
    git://codeaurora.org/quic/le/platform/external/tinyalsa.git;protocol=git;branch=github/master \
    file://Makefile.am \
    file://configure.ac \
    file://tinyalsa.pc.in \
    file://0001-tinyhostless.patch \
"

SRCREV = "a36069e2b551db4f3be3a0bc4f0f38bc3f0d1899"

S = "${WORKDIR}"

inherit autotools pkgconfig

EXTRA_OEMAKE = "DEFAULT_INCLUDES=-I${WORKDIR}/git/include/"
