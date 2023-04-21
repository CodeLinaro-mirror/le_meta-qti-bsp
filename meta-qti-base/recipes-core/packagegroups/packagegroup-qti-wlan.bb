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
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', 'qcacld32-ll-hasting-cnss0', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', 'qcacld32-ll-hasting-cnss2', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', '', 'qcacld32-ll-hasting', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', '', 'qcacld32-ll-genoa', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', '', 'qcacld32-ll-rome', d)} \
    wlan-sigma-dut \
    wpa-supplicant \
    wlan-conf \
    "
