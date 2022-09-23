require perl-rdepends_${PV}.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "\
        file://makedepend.SH.patch \
        "
