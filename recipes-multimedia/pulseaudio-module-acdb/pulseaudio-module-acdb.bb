inherit autotools-brokensep pkgconfig

DESCRIPTION = "Pluseaudio module for audio calibration data"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
PR = "r0"

DEPENDS = "glib-2.0 pulseaudio acdbloader audcal"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LDFLAGS += " -ldl -ljson-c"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:"
SRC_URI = "file://audio/mm-audio-opensource/pulseaudio-module-acdb/"
S = "${WORKDIR}/audio/mm-audio-opensource/pulseaudio-module-acdb/"

SRC_URI += "file://8x96autogvmga/pulseaudio-acdb.cfg"

EXTRA_OECONF += "--with-glib \
                 --enable-target=${BASEMACHINE}"

FILES_${PN} += "${libdir}/pulse-8.0/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-8.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-8.0/modules/.debug"

do_install_append() {
         mkdir -p ${D}${sysconfdir}/pulse/
         install -m 0755 ${S}/*.cfg  -D ${D}${sysconfdir}/pulse/
}

do_install_append_8x96autogvmga() {
         # override with the machine type specific config
         install -m 0755 ${WORKDIR}/8x96autogvmga/pulseaudio-acdb.cfg -D ${D}${sysconfdir}/pulse/
}
