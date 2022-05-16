FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI:append = " \
    file://root-home.patch \
    file://add-hash.patch \
    file://add-diag-user.patch \
    file://add-sdcard-diag-groups.patch \
    file://add-reboot-daemon-group.patch \
    file://add-inet-group-tinyproxy.patch \
"

PR = "r1"
