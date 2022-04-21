SUMMARY = "QTI package group for audio"
DESCRIPTION = "This is the minimal set of packages required for audio kernel modules and audio initialization scripts."
LICENSE = "GPL-2.0 & BSD"

inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS:${PN} = "\
    alsa-lib \
    alsa-utils \
    audiodlkm \
    init-audio \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', 'agm agm-client agm-plugin agm-server agm-sndparser', '', d)} \
"