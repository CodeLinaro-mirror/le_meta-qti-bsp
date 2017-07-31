inherit qcommon cmake

SUMMARY = "sdm library"
SECTION  = "sdm"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRCREV = "${AUTOREV}"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/sdm"

S = "${WORKDIR}/display/sdm"

SRC_DIR = "${WORKSPACE}/display/sdm"

DEPENDS += "libcutils"

EXTRA_OECMAKE = "-DDISPLAY_HEADER_INC:STRING=${WORKSPACE}/display/include"

do_install () {

    if [ -d "${SRC_DIR}" ]; then
        cmake_do_install
    fi

    #public sdm header files
    echo "install to ${D}${includedir}"
    install -d ${D}${includedir}
    install -d ${D}${includedir}/sdm/include/utils
    install -d ${D}${includedir}/sdm/include/private
    install -d ${D}${includedir}/sdm/include/core

    install ${S}/include/utils/*  ${D}${includedir}/sdm/include/utils
    install ${S}/include/private/*  ${D}${includedir}/sdm/include/private
    install ${S}/include/core/*  ${D}${includedir}/sdm/include/core

    install ${WORKSPACE}/display/include/*  ${D}${includedir}/
}

FILES_${PN} += " \
   ${libdir}/* \
   ${includedir} \
   ${includedir}/sdm/include/utils \
   ${includedir}/sdm/include/private \
   ${includedir}/sdm/include/core \
"

PACKAGES = "${PN}"

INHIBIT_PACKAGE_STRIP="1"
INHIBIT_PACKAGE_DEBUG_SPLIT="1"
