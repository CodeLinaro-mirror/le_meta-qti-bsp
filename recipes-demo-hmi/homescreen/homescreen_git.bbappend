FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-Only-display-AGL-demos-on-homescreen.patch"

SRC_URI = "git://source.codeaurora.org/quic/le/AGL/apps/homescreen.git;protocol=https;branch=apps/automotivelinux/homescreen/${AGL_BRANCH} \
           file://dbus-homescreen.conf.in"
