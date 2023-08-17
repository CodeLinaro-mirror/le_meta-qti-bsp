SUMMARY = "Grouping of programs for userspace drivers on Linux System"
DESCRIPTION = "Package group to support userspace drivers"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = " \
    packagegroup-qti-lvumd \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} = "\
    safelinux-cfg-modules \
"
