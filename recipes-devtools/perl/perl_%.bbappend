require perl-rdepends_${PV}.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "\
	file://makedepend.SH.patch \
	file://CVE-2018-18311.patch \
	file://CVE-2018-18314_1.patch \
        "
