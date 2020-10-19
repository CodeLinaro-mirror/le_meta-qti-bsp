SUMMARY = "QTI package group for audio"
DESCRIPTION = "This is the minimal set of packages required for audio kernel modules and audio initialization scripts."
LICENSE = "GPL-2.0 & BSD"

inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    init-audio \
    audiodlkm \
    alsa-lib \
    alsa-utils \
"
