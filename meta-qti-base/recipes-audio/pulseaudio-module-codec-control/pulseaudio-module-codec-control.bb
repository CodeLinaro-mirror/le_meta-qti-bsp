DESCRIPTION = "pulseaudio module for codec control"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS = "glib-2.0 pulseaudio audio-hal-plugin-noship"
SRCREV = "${AUTOREV}"
PR = "r0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/mm-audio/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control;subpath=pulseaudio-module-codec-control;usehead=1"

S = "${WORKDIR}/vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control/"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "--enable-target=${AUDIO_BUILD_TARGET}"
EXTRA_OECONF += "--with-glib"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = "pulseaudio-server pulseaudio-misc pulseaudio-module-null-source"

FILES_${PN} += "${libdir}/pulse-${PULSE_PV}/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-${PULSE_PV}/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-${PULSE_PV}/modules/.debug"

AUDIO_BUILD_TARGET ?= "sa8155"
PULSE_PV = "13.0"
