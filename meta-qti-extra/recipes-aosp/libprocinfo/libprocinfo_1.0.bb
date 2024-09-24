SUMMARY = "libprocinfo utility"
DESCRIPTION = "libprocinfo provide API for parsing /proc info"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0 & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
LIC_FILES_CHKSUM += "file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"
DEPENDS = "libbase libcutils libutils gtest"

SRC_URI = "${CLO_LA_GIT}/platform/system/libprocinfo;name=native;protocol=https;branch=aosp-new/android12-gsi;destsuffix=git \
           file://libprocinfo-port.patch \
           file://0001-libprocinfo-Add-automake-support.patch \
          "
# matches with android11-mainline-release release
SRCREV = "86e630123f122225313adc8b02008210c0c62d28"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

