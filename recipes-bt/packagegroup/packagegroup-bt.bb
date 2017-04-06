SUMMARY = "BT Deamons"
LICENSE = "BSD"
PR = "r1"

inherit packagegroup

RDEPENDS_${PN} = "\
    bt-property \
    bt-app \
    fluoride \
    libbt-vendor \
    "
