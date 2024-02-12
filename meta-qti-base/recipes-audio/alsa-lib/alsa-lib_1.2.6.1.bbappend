SRC_URI:append = " file://0001-alsa_lib-support-mmap-mode.patch"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:${THISDIR}/${BPN}-${PV}:"
