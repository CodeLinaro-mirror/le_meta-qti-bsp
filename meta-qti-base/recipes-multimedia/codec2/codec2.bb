SUMMARY = "Codec2 multimedia framework"
DESCRIPTION = "Codec2 is a codec framework introduced by Google. This is to eventually replace OpenMax-IL."
HOMEPAGE = "https://www.codeaurora.org/"
SECTION = "multimedia"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "clang-native \
            display-commonsys-intf-linux \
            gbm \
            gbm-headers \
            libcutils \
            libion \
            libutils \
            media-plugin-headers \
            system-core-headers"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks/av/media/codec2;usehead=1"

S = "${WORKDIR}/frameworks/av/media/codec2"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

inherit cmake

do_configure[depends] += "virtual/kernel:do_shared_workdir"

PACKAGE_ARCH = "${MACHINE_ARCH}"


CXXFLAGS += "-I${STAGING_INCDIR} \
             -I${STAGING_KERNEL_BUILDDIR}/include \
             -I${STAGING_KERNEL_BUILDDIR}/usr/include \
             -I${STAGING_INCDIR}/drm \
             -I${STAGING_INCDIR}/ion_headers \
             -I${STAGING_INCDIR}/kernel-headers \
             -I${STAGING_INCDIR}/c++/ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS}"

TOOLCHAIN = "clang"
