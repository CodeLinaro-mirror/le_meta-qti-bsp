FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI += "\
        file://CVE-2017-16931.patch \
	file://CVE-2017-16932.patch \
"
