SUMMARY = "QTI package group for security"
DESCRIPTION = "This is the minimal set of packages required for linux userspace security utilities."

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-security \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    libcap \
    libcap-bin \
    attr \
    ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "securemsmdlkm", "", d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-fde', 'enable-fde', '', d)} \
"
