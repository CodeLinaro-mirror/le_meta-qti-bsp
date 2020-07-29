SUMMARY = "Tinycompress Library"
DESCRIPTION = "ALSA sound library for compress format."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause | LGPL-2.1"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
    file://${COREBASE}/meta/files/common-licenses/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780 \
"

SRC_URI = "git://codeaurora.org/quic/le/platform/external/tinycompress.git;protocol=git;branch=alsa-project/master"

SRCREV = "e3edd98993f0710043381d71cf3c0bf497b0ebb1"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
