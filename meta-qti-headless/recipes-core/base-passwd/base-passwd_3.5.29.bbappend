FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI:append = " \
    file://add-diag-user.patch \
    file://add-sdcard-diag-groups.patch \
    file://add-inet-group-tinyproxy.patch \
"

PR = "r1"
