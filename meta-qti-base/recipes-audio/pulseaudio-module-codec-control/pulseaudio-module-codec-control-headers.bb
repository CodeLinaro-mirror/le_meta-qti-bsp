
DESCRIPTION = "Pluseaudio codec control module header files"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
PR = "r0"

SRC_URI = "git://source.codeaurora.org/platform/vendor/qcom-opensource/mm-audio.git;protocol=https;destsuffix=vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control;subpath=pulseaudio-module-codec-control;nobranch=1"
SRCREV  = "a4cd8bb0e14d2ee8e8222def7719dfd732d71574"

S = "${WORKDIR}/vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control/"


do_install() {
    install -d ${D}${includedir}
    cp -r ${S}/inc/interface/*  ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
