inherit autotools

DESCRIPTION = "Installing audio init script"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
PR = "r5"

DEPENDS_append_mdm9635 +="alsa-intf"

SRC_URI = "file://init_qti_audio"
SRC_URI_msm8974 = "file://${BASEMACHINE}/init_qti_audio"
SRC_URI_msm8610 = "file://${BASEMACHINE}/init_qti_audio"

S = "${WORKDIR}"
S_msm8974 = "${WORKDIR}/${BASEMACHINE}"
S_msm8610 = "${WORKDIR}/${BASEMACHINE}"

INITSCRIPT_NAME_8x96auto = "init_qti_audio"
INITSCRIPT_PARAMS_8x96auto = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96mizar = "init_qti_audio"
INITSCRIPT_PARAMS_8x96mizar = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autodvrs = "init_qti_audio"
INITSCRIPT_PARAMS_8x96autodvrs = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autofusion = "init_qti_audio"
INITSCRIPT_PARAMS_8x96autofusion = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autogvmquin = "init_qti_audio"
INITSCRIPT_PARAMS_8x96autogvmquin = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autogvmquintcu = "init_qti_audio"
INITSCRIPT_PARAMS_8x96autogvmquintcu = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autogvmred = "init_qti_audio"
INITSCRIPT_PARAMS_8x96autogvmred = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autonapier = "init_qti_audio"
INITSCRIPT_PARAMS_8x96autonapier = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96autogvmgh = "init_qti_audio"
INITSCRIPT_PARAMS_8x96autogvmgh = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_msm8996 = "init_qti_audio"
INITSCRIPT_PARAMS_msm8996 = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_8x96mctm = "init_qti_audio"
INITSCRIPT_PARAMS_8x96mctm = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_msm8974 = "init_qti_audio"
INITSCRIPT_PARAMS_msm8974 = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_msm8610 = "init_qti_audio"
INITSCRIPT_PARAMS_msm8610 = "start 99 2 3 4 5 . stop 1 0 1 6 ."

do_install() {
    install -m 0755 ${S}/init_qti_audio -D ${D}${sysconfdir}/init.d/init_qti_audio
}

do_install_msm8974() {
    install -m 0755 ${S}/${BASEMACHINE}/init_qti_audio -D ${D}${sysconfdir}/init.d/init_qti_audio
}

do_install_msm8610() {
    install -m 0755 ${S}/${BASEMACHINE}/init_qti_audio -D ${D}${sysconfdir}/init.d/init_qti_audio
}


INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'init-audio_auto', 'none',d)}"
include ${INCSUFFIX}.inc

