SUMMARY = "Google's framework for writing C++ tests"
DESCRIPTION = "Gtest is a cross-platform C++ testing framework published by Google"
HOMEPAGE = "http://code.google.com/p/googletest/"
SECTION = "libs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=cbbd27594afd089daa160d3a16dd515a"

SRC_URI = "\
    git://source.codeaurora.org/quic/le/external/oracle/gtest;protocol=git;nobranch=1 \
"

SRCREV = "d850e144710e330070b756c009749dc7a7302301"

S = "${WORKDIR}/git"

inherit lib_package cmake

EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"

BBCLASSEXTEND = "native nativesdk"
