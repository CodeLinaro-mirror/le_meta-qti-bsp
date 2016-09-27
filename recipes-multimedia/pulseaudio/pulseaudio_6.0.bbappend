FILESEXTRAPATHS_prepend := "${THISDIR}/pulseaudio:"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'zeroconf', 'avahi', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', '3g', 'ofono', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
                   "

SRC_URI += " \
             file://0001-disable-timer-based-scheduling.patch \
	     file://0002-default.pa-Load-acdb-and-codec-control-modules.patch \
           "
