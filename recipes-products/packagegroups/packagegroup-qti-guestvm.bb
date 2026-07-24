SUMMARY = "Package group required to support Android GVM"
DESCRIPTION = "Package group to bring in all necessary components including qcrosvm and qcvirtio-devices to support Android GVM"
LICENSE = "BSD-3-Clause-Clear"

inherit packagegroup

PROVIDES = "${PACKAGES}"
RPROVIDES:${PN} = "${PACKAGES}"

PACKAGES = ' \
    ${PN} \
    '

RDEPENDS:${PN} = "\
    vhost-user-lib \
    qcrosvm \
    adbd-relay \
    "
