SUMMARY = "QTI package group for audio"
DESCRIPTION = "This is the minimal set of packages required for audio kernel modules and audio initialization scripts."
LICENSE = "GPL-2.0 & BSD"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "${PN}"

# qti-audio is for elite framework, qti-audio-ar is for AR framework
RDEPENDS:${PN} = "\
    alsa-lib \
    alsa-utils \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio', 'audiodlkm', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', 'agm agm-client agm-alsa-plugin agm-server agm-sndparser', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'init-audio', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'ar-audiodlkm', d), '', d)} \
"

RDEPENDS:${PN}:append:qti-dpk = " \
    agm-tinyalsa-plugin \
    ar-pal \
    pal-control-plugin \
    system-media \
    tinyalsa \
    tinycompress \
"
