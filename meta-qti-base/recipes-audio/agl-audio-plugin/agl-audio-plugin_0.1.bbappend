SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agl-audio-plugin/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agl-audio-plugin;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/agl-audio-plugin"

EXTRA_OECMAKE += "\
    -DTARGET_BOARD_PLATFORM=${BASEMACHINE} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', '-DAUDIO_ARCH=audio_reach', '', d)} \
    "
FILES_${PN} += "${libdir}/pulse-*/modules/*"
FILES_${PN}-dbg += "${libdir}/pulse-*/modules/.debug/*"
