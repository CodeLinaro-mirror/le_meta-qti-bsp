FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI += "\
        file://CVE-2016-9843.patch \
        file://CVE-2016-9841.patch \
"
