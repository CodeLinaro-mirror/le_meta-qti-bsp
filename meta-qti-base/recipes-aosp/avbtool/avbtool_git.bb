inherit autotools pkgconfig

SUMMARY = "avbtool: Image signing tool"
DESCRIPTION = "Image signing tool used for generating images needed to support Android Verified Boot 2.0."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "MIT & BSD-3-Cluase & Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b8228f2369d92593f53f0a0685ebd3c0"

SRC_URI = "${CLO_LA_GIT}/platform/external/avb;protocol=https;branch=aosp-new/sdk-release"
SRC_URI += " \
    file://0002-libavb-Add-Makefile.am-and-configure.ac-for-libavb-t.patch \
    file://0003-libavb-modify-libavb-to-align-with-container-avb.patch \
"
SRCREV = "47c41533d44b0cc499a1ccccac4de1a565bdb8c2"
S = "${WORKDIR}/git"

inherit autotools pkgconfig
