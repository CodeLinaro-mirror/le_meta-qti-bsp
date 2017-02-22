inherit native deploy
inherit qcommon

DESCRIPTION = "Pack uncompressed kernel and DTBs "
LICENSE     = "Apache-2.0"
PR = "r1"

LIC_FILES_CHKSUM = " \
   file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

FILESPATH =+ "${WORKSPACE}:"
SRC_DIR = "${WORKSPACE}/android_compat/device/qcom/common/packkernelimg/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/', '')}"

S = "${WORKDIR}/android_compat/device/qcom/common/packkernelimg"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/packkernelimg ${D}${bindir}/
}

do_deploy() {
    install -m 0755 ${S}/packkernelimg ${DEPLOYDIR}/
}

addtask deploy before do_build after do_install
