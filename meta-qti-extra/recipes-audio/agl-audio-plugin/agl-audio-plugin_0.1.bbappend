PULSE_PV="13.0"

SRC_URI = "git://source.codeaurora.org/quic/le/AGL/staging/agl-audio-plugin.git;protocol=https;destsuffix=vendor/qcom/opensource/agl-audio-plugin;nobranch=1"
SRCREV  = "a7f10745df3823c39125b7d7c19474af99b001a4"

S = "${WORKDIR}/vendor/qcom/opensource/agl-audio-plugin/"

EXTRA_OECMAKE += "-DTARGET_BOARD_PLATFORM=${BASEMACHINE}"
