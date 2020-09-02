inherit qlicense qcommon
DESCRIPTION = "Provide native media hardware Headers"

SRC_URI = "git://source.codeaurora.org/quic/le/platform/vendor/qcom-opensource/le-framework.git;protocol=${PROTO};destsuffix=frameworks;nobranch=1"
S = "${WORKDIR}/frameworks"
SRCREV = "512dafe851af504ac4642acbd25936aa232711a4"

do_install() {
    install -d ${D}${includedir}/media/hardware
    install -m 0644 ${S}/native/include/media/hardware/*.h -D ${D}${includedir}/media/hardware/
}

ALLOW_EMPTY_${PN} = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

