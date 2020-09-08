FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI += "\
        file://CVE-2017-15804.patch \
	file://CVE-2017-15670.patch \
	file://CVE-2017-1000366.patch \
	file://CVE-2015-5180.patch \
	file://CVE-2017-12133.patch \
	file://CVE-2019-9169.patch \
	file://CVE-2018-6485.patch \
	file://CVE-2018-1000001.patch \
"
