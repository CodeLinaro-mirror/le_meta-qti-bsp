SUMMARY = "Grouping of programs for running VMs on Embedded Linux System"
DESCRIPTION = "Package group to bring in packages for running VMs"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = " \
    packagegroup-qti-qcrosvm \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} = "\
    qcrosvm \
    "
