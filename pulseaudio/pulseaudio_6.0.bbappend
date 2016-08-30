FILESEXTRAPATHS_prepend := "${THISDIR}/pulseaudio:"

SRC_URI += " \
             file://0001-disable-timer-based-scheduling.patch \
           "
