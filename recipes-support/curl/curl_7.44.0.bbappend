FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI += "\
        file://CVE-2017-8816.patch \
        file://CVE-2016-7167.patch \
        file://CVE-2018-14618.patch \
"
