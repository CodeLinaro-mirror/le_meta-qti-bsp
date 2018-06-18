inherit qcommon cmake

SUMMARY = "ARM Compute Library"
SECTION = "multimedia"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
# fetch from server
SRC_URI = "git://source.codeaurora.org/quic/le/vendor/arm/ComputeLibrary;protocol=https;branch=ComputeLibrary/master"

SRC_URI += "file://0001-ComputeLibrary-add-cmakelist-and-install-directives.patch"
SRC_URI += "file://0002-computelibrary-allocate-tensors-from-host-pointer.patch"

# commit "8a3da6f91f90c566b844d568f4ec43b946915af8" > tested upstream version
SRCREV = "8a3da6f91f90c566b844d568f4ec43b946915af8"

S      = "${WORKDIR}/git"

DEPENDS += "adreno"

INSANE_SKIP_${PN} += "dev-deps"

do_install_append() {
    rm -f ${D}${libdir}/*.a
}

FILES_${PN} += "${libdir}/*.so"
INSANE_SKIP_${PN}-dev += "dev-elf"

ALLOW_EMPTY_${PN} = "1"
