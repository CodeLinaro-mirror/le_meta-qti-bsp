SUMMARY = "QTI package group for security"
DESCRIPTION = "This is the minimal set of packages required for linux userspace security utilities."

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-security \
"

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    libcap \
    libcap-bin \
    attr \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'enable-fde', '', d)} \
"
