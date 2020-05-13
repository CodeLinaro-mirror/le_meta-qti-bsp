FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI += "\
        file://CVE-2017-8816.patch \
"
