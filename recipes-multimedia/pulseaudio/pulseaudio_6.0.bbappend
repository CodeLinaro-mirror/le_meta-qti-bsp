FILESEXTRAPATHS_prepend := "${THISDIR}/pulseaudio:"

SRC_URI += " \
             file://0001-disable-timer-based-scheduling.patch \
	     file://0002-default.pa-Load-acdb-and-codec-control-modules.patch \
           "
