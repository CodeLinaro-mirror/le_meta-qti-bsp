DESCRIPTION = "Header-only library for conversion to/from half-precision floating point formats."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
HOMEPAGE = "https://github.com/Maratyszcza/FP16"

PR = "r0"

SRC_URI = "git://github.com/Maratyszcza/FP16.git;protocol=https;name=FP16"
SRCREV = "4dfe081cf6bcd15db339cf2680b9281b8451eeb3"
S = "${WORKDIR}/git"

do_compile[noexec] = "1"

do_install() {
	install -d ${D}${includedir}/fp16
	install -m 0644 ${S}/include/fp16/fp16.h ${D}${includedir}/fp16/
	install -m 0644 ${S}/include/fp16/bitcasts.h ${D}${includedir}/fp16/
}

PACKAGES = "${PN}"
FILES_${PN} += "${includedir}"
