HOMEPAGE = "https://github.com/google/googletest"
LIC_FILES_CHKSUM = "file://googlemock/LICENSE;md5=cbbd27594afd089daa160d3a16dd515a \
                    file://googletest/LICENSE;md5=cbbd27594afd089daa160d3a16dd515a"

PROVIDES += "gmock"

SRC_URI = "\
    ${CLO_LE_GIT}/external/oracle/gtest;protocol=${CLO_PROTOCOL};nobranch=1 \
"
SRCREV = "d850e144710e330070b756c009749dc7a7302301"

FILES_${PN} += "${libdir}/*.so"

ALLOW_EMPTY_${PN} = "1"
ALLOW_EMPTY_${PN}-dbg = "1"
ALLOW_EMPTY_${PN}-staticdev = "1"

RDEPENDS_${PN}-dev += "${PN}-staticdev"
