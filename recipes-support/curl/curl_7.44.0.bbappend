FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI += "\
        file://CVE-2017-8816.patch \
        file://CVE-2016-7167.patch \
        file://CVE-2018-14618.patch \
        file://CVE-2017-1000257.patch \
        file://CVE-2018-1000122.patch \
        file://CVE-2018-1000301.patch \
        file://CVE-2019-5436.patch \
"
