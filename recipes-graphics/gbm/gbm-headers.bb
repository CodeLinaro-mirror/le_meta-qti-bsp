inherit qcommon 
DESCRIPTION = "gbm-headers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=3775480a712fc46a69647678acb234cb"
PV = "1.0+git"
PR = "r2"

SRCREV="${AUTOREV}"
PREBUILT = "1"
SRC_URI = "file://libgbm/"
SRC_URI += "https://source.codeaurora.org/quic/lc/chromiumos/third_party/mesa/plain/src/gbm/main/gbm.h?h=chromium.org/arc-11.3.0-pre1&id=0be59ca79eddc5cc6aa6aee70f40678a5b021bd6;downloadfilename=gbm.h;md5sum=f660942fecae678d3028baedfbb76b0f;sha256sum=a6025e23490e74345df70b29e8b4bd144c3e9dc6bd452c921b29df38f3aaafc4"
COLOR_METADATA_DIR ?= ""
COLOR_METADATA_DIR_8x96auto = "${WORKSPACE}/display/display-hal/"
COLOR_METADATA_DIR_8x96autodvrs = "${WORKSPACE}/display/display-hal/"

S = "${WORKDIR}/libgbm"

do_configure[noexec] = "1"
do_compile[noexec]   = "1"

do_install(){
    install -d ${D}${includedir}
    install ${S}/../gbm.h ${D}${includedir}
    if [ "${MACHINE}" == "8x96auto" ] || [ "${MACHINE}" == "8x96autogvmquin" ] || [ "${MACHINE}" == "8x96autodvrs" ]; then
        install ${WORKSPACE}/display/display-hal/include/color_metadata.h ${D}${includedir}
    fi
}

FILES_${PN} = "${includedir}/*"

PACKAGES = "${PN}"

