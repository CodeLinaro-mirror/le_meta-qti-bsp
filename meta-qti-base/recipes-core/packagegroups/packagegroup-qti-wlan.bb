SUMMARY = "QTI package group for wlan"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-wlan \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    rfkill \
    hostap-daemon-qcacld \
    wireless-tools \
    iw \
    ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', 'wlan-platform-dlkm', '', d)} \
    qcacld32-ll-genoa \
    qcacld32-ll-hasting-cnss0 \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', 'qcacld32-ll-hasting-cnss2', '', d)} \
    qcacld32-ll-hsp \
    qcacld32-ll-rome \
    wlan-sigma-dut \
    wpa-supplicant \
    wlan-conf \
    cnss-wlan-load \
    "
