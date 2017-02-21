inherit autotools-brokensep pkgconfig

DESCRIPTION = "Pluseaudio codec control module"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
PR = "r0"

DEPENDS = "glib-2.0 pulseaudio audio-hal-plugin-noship"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:"
SRC_URI = "file://audio/mm-audio-opensource/pulseaudio-module-codec-control/"
S = "${WORKDIR}/audio/mm-audio-opensource/pulseaudio-module-codec-control/"

EXTRA_OECONF += "--with-glib \
                 --enable-target=${BASEMACHINE}"

FILES_${PN} += "${libdir}/pulse-8.0/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-8.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-8.0/modules/.debug"
