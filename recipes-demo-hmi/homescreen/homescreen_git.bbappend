FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-Only-display-AGL-demos-on-homescreen.patch"

SRC_URI = "${CLO_LE_GIT}/AGL/apps/homescreen.git;protocol=${CLO_PROTOCOL};nobranch=1;name=homescreen \
           file://dbus-homescreen.conf.in"

SRCREV_homescreen = "69be38c5d975c96eee7adab238ffd608b99d8514"
