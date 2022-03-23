SUMMARY = "QTI package group for audio"
DESCRIPTION = "This is the minimal set of packages required for audio kernel modules and audio initialization scripts."
LICENSE = "GPL-2.0 & BSD"

inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    alsa-lib \
    alsa-utils \
    init-audio \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio', 'audiodlkm', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', bb.utils.contains('MACHINE_FEATURES','qti-hypervisor','agm agm-client agm-plugin agm-server agm-sndparser', 'ar-audiodlkm agm agm-client agm-plugin agm-server agm-sndparser',d), '',d)} \
"
