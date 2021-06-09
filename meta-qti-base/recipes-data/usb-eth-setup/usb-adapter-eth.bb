SUMMARY = "Udev rule to trigger a script when plugin an usb adpter"
DESCRIPTION = "Qualcomm Technologies, Inc. Binary to setup interface after plugin usb-to-ethernet adapter"
HOMEPAGE = "https://www.codeaurora.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "\
    file://usb_adapter.sh \
    file://85-usb-adapter.rules \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
        install -d ${D}${sysconfdir}/udev/rules.d/
        install -m 0444 ${WORKDIR}/85-usb-adapter.rules ${D}${sysconfdir}/udev/rules.d/85-usb-adapter.rules
        install -d ${D}${sysconfdir}/udev/scripts/
        install -m 0555 ${WORKDIR}/usb_adapter.sh ${D}${sysconfdir}/udev/scripts/usb_adapter.sh
}

RDEPENDS_${PN} += "bash"
