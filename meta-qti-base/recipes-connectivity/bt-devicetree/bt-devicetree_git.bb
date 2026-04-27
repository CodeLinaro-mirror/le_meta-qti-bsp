SUMMARY = "QTI Bluetooth devicetree"
DESCRIPTION = "Build QTI Bluetooth devicetree to dtbo"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SOURCE_PATH = "vendor/qcom/opensource/bt-devicetree"
SRC_URI = "${PATH_TO_REPO}/${SOURCE_PATH}/.git;protocol=${PROTO};destsuffix=${SOURCE_PATH};usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/${SOURCE_PATH}"

EXT_MODULE = "${SOURCE_PATH}"

inherit qti-techpack

do_configure[noexec] = "1"

TECHPACK_MODULE_OUT = "${WORKDIR}/bt-devicetree"
TECHPACK_DTBS:append:gvm-gen5 = " \
                 sa8797p-gunyah-vm-bt.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-lemans = " \
                 lemans-bt.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-gen4-5 = " \
                 lemans-bt.dtbo \
"
TECHPACK_DTBS:append:gvm-gen4-5 = " \
                 lemans-bt.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-gen4-2 = " \
                 direwolf-bt.dtbo \
"
TECHPACK_DTBS:append:qtiquingvm8295 = " \
                 direwolf-bt.dtbo \
"
TECHPACK_DTBOS:append:monaco = " \
                 monaco-bt.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-gen4 = " \
                 direwolf-bt.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-monaco = " \
                 monaco-bt.dtbo \
"
