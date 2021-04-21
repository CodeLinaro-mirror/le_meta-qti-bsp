SUMMARY = "PulseAudio Module ACDB"
DESCRIPTION = "This is the PulseAudio module used for audio calibration."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS = "glib-2.0 pulseaudio acdbloader audcal json-c"
SRCREV = "${AUTOREV}"
PR = "r0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/mm-audio/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/mm-audio/pulseaudio-module-acdb;subpath=pulseaudio-module-acdb;usehead=1"

S = "${WORKDIR}/vendor/qcom/opensource/mm-audio/pulseaudio-module-acdb"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "--enable-target=${AUDIO_BUILD_TARGET}"
EXTRA_OECONF += "--enable-acdbservice=yes"
EXTRA_OECONF += "--with-glib"

do_install_append() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0755 ${S}/*.cfg -D ${D}${sysconfdir}/pulse
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = "acdbloader"

FILES_${PN} += "${libdir}/pulse-*/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-*/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-*/modules/.debug"

AUDIO_BUILD_TARGET ?= "sa8155"
