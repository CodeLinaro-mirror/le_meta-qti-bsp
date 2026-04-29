SUMMARY = "Hyp udmabuf test"
DESCRIPTION = "This is the hyp udmabuf test used to test hyp dmabuf"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"


FILESPATH   =+ "${WORKSPACEROOT}:"
SRC_URI = "file://vendor/qcom/opensource/hyp-udmabuf/test/"

S = "${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/test"

EXTRA_OECMAKE += "-DTARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM}"

inherit cmake

do_configure:prepend() {
    install -d ${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/drivers/include/uapi/linux/
    install -m 0644 ${WORKSPACEROOT}/vendor/qcom/opensource/hyp-udmabuf/drivers/include/uapi/linux/hyp_udmabuf.h \
        ${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/drivers/include/uapi/linux/
}