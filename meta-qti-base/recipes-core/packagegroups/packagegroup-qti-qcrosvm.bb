SUMMARY = "Package group to bring in packages for running VMs"
DESCRIPTION = "Grouping of programs for running VMs on Embedded Linux System"

PROVIDES = "${PACKAGES}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-qcrosvm \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} = "\
    qcrosvm \
    vhost-user-q \
    safelinux-system-cfg \
    dspfirmware-mount \
"
