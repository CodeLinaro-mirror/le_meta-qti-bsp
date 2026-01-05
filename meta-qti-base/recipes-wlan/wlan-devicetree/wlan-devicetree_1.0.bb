SUMMARY = "Wlan devicetree"
DESCRIPTION = "Build Wlan devicetree to dtbo"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SOURCE_PATH = "vendor/qcom/opensource/wlan"
SRC_URI = "${PATH_TO_REPO}/${SOURCE_PATH}/wlan-devicetree/.git;protocol=${PROTO};destsuffix=${SOURCE_PATH}/wlan-devicetree;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/${SOURCE_PATH}/wlan-devicetree"

inherit qti-techpack
EXT_MODULE = "${SOURCE_PATH}/wlan-devicetree"

do_configure[noexec] = "1"

TECHPACK_MODULE_OUT = "${WORKDIR}/wlan-devicetree"
TECHPACK_DTBS:append:sa81x5 = " \
                 sa8155p-cnss.dtbo \
                 sa8195p-cnss.dtbo \
"
TECHPACK_DTBOS:append:monaco = " \
                 monaco_auto-cnss.dtbo \
"
TECHPACK_DTBS:quin-gvm-lemans = "\
                 lemans-vm-cnss.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-gen4-2 = " \
                 direwolf-vm-cnss.dtbo \
"
TECHPACK_DTBS:append:qtiquingvm8295 = " \
                 direwolf-vm-cnss.dtbo \
"
TECHPACK_DTBS:quin-gvm-monaco = "\
                 monaco_auto-vm-cnss.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-gen4 = " \
                direwolf-vm-cnss.dtbo \
"
TECHPACK_DTBS:append:quin-gvm-gen4-5 = " \
                lemans-vm-cnss.dtbo \
"
