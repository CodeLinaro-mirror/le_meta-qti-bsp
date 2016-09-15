inherit autotools qcommon

DESCRIPTION = "Pluseaudio codec control module"
PR = "r0"

DEPENDS = "glib-2.0 pulseaudio audio-hal-plugin-noship"

SRC_DIR = "${WORKSPACE}/audio/mm-audio-opensource/pulseaudio-module-codec-control/"
S = "${WORKDIR}/audio/mm-audio-opensource/pulseaudio-module-codec-control/"

EXTRA_OECONF += "--with-glib \
                 --enable-target=${BASEMACHINE}"

FILES_${PN} += "${libdir}/pulse-6.0/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-6.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-6.0/modules/.debug"
