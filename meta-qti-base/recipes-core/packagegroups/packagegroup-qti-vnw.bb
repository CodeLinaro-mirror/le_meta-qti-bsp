SUMMARY = "QTI package group for vichle network"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-vnw \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    open-avb \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'ptp-virtual', '', d)} \
    hsi2s \
    hsi2s-test \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'avb-utils', '', d)} \
    "
