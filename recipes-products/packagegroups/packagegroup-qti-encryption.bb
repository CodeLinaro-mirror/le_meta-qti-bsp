SUMMARY = "Basic programs and scripts required to encrypt userdata"
DESCRIPTION = "Package group to bring in all basic packages for userdata encryption"
LICENSE = "BSD-3-Clause-Clear"

inherit packagegroup

PROVIDES = "${PACKAGES}"
RPROVIDES:${PN} = "${PACKAGES}"

PACKAGES = ' \
    ${PN} \
    '

RDEPENDS:${PN} = "\
    cryptsetup \
    e2fsprogs-mke2fs \
    cryptinit \
    "
