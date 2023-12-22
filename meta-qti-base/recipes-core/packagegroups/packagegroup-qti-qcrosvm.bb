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
    gunyah-drivers \
    msmhab \
    vhost-user-q \
    vhost-user-lib \
    safelinux-cfg-modules \
    safelinux-system-cfg \
    safelinux-dbg-modules \
    dspfirmware-mount \
    gvm-net-config \
"
