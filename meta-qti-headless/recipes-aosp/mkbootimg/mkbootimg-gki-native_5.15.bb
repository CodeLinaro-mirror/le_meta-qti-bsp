SUMMARY = "Tool used for creating boot image"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PROVIDES = "mkbootimg-native"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/kernel/kernel-5.15/kernel_platform/tools/mkbootimg/.git;protocol=${PROTO};destsuffix=kernel/kernel-5.15/kernel_platform/tools/mkbootimg;usehead=1"

S = "${WORKDIR}/kernel/kernel-5.15/kernel_platform/tools/mkbootimg"

inherit native

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}/${bindir}/scripts/
    install -m 0755 ${S}/mkbootimg.py ${D}/${bindir}/scripts/
    install -d ${D}/${bindir}/scripts/gki/
    install -m 0755 ${S}/gki/generate_gki_certificate.py ${D}/${bindir}/scripts/gki/
}
