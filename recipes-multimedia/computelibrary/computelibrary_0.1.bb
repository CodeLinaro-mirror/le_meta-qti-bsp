inherit qcommon cmake

SUMMARY = "ARM Compute Library"
SECTION = "multimedia"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
# fetch from server
SRC_URI = "git://source.codeaurora.org/quic/le/vendor/arm/ComputeLibrary;protocol=http;branch=ComputeLibrary/master"

SRC_URI += "file://0001-ComputeLibrary-add-cmakelist-and-install-directives.patch"
SRC_URI += "file://0002-ComputeLibrary-add-absolute-path-for-cl2.hpp.patch"

# commit 68a98dc29106a4c8c34e8cac542fe0bdad4ad531 > tested upstream version
SRCREV = "68a98dc29106a4c8c34e8cac542fe0bdad4ad531"

S      = "${WORKDIR}/git"

DEPENDS += "adreno"

INSANE_SKIP_${PN} += "dev-deps"

do_install_append() {
    rm -f ${D}${libdir}/*.a
}

FILES_${PN} += "${libdir}/*.so"
INSANE_SKIP_${PN}-dev += "dev-elf"
