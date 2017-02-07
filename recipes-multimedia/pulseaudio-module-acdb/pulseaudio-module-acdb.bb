inherit autotools qcommon

DESCRIPTION = "Pluseaudio module for audio calibration data"
PR = "r0"

DEPENDS = "glib-2.0 pulseaudio acdbloader audcal"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_DIR = "${WORKSPACE}/audio/mm-audio-opensource/pulseaudio-module-acdb/"
S = "${WORKDIR}/audio/mm-audio-opensource/pulseaudio-module-acdb/"

EXTRA_OECONF += "--with-glib \
                 --enable-target=${BASEMACHINE}"

FILES_${PN} += "${libdir}/pulse-6.0/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-6.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-6.0/modules/.debug"
