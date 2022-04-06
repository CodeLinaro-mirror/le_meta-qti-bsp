inherit autotools 

DESCRIPTION = "bootctrl  utility."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS_prepend := "${THISDIR}:"

SRC_URI = "https://git.codelinaro.org/clo/la/platform/system/extras/-/raw/373d3c7257fa815d0b9ee8f16874470a6002042e/bootctl/bootctl.cpp;downloadfilename=bootctl.cpp;protocol={CLO_PROTOCOL};name=bootctl"
SRC_URI += " file://0001-Enabel-bootctl-for-LV.patch "

S = "${WORKDIR}/${PN}"

do_move_bootctl() {
    cp ${WORKDIR}/bootctl.cpp ${S}
}
do_patch[prefuncs] += "do_move_bootctl"

DEPENDS += "oem-recovery libhardware liblog libcutils libbootctrl"
RDEPENDS_${PN} += "libbootctrl"
