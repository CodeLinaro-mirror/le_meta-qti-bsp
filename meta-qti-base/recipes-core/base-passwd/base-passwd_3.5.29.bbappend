FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI:append = " \
    file://root-home.patch \
    file://add-hash.patch \
"

PR = "r1"
