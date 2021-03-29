SUMMARY = "init script for kdump to dump crash data in capture kernel"
SECTION = "kdump"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://kdump-initscript.sh"

S = "${WORKDIR}"

do_install() {
	install -m 0755 ${WORKDIR}/kdump-initscript.sh ${D}/init
}

inherit allarch

FILES_${PN} += " /init "
