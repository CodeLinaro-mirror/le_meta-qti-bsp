SUMMARY = "Codec2 multimedia framework"
DESCRIPTION = "Codec2 is a codec framework introduced by Google. This is to eventually replace OpenMax-IL."
HOMEPAGE = "https://www.codeaurora.org/"
SECTION = "multimedia"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/frameworks/NOTICE;md5=a3fcbe20ea5ac731ed3aa15fe59ba20a"

DEPENDS += "clang-native \
            display-commonsys-intf-linux \
            gbm \
            gbm-headers \
            libcutils \
            libion \
            libutils \
            linux-msm-headers \
            media-plugin-headers \
            system-core-headers"

SRCREV = "${AUTOREV}"
SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks/av/media/codec2;usehead=1"

S = "${WORKDIR}/frameworks/av/media/codec2"

inherit cmake

CXXFLAGS += "-I${STAGING_INCDIR} \
             -I${STAGING_INCDIR}/linux-msm \
             -I${STAGING_INCDIR}/drm \
             -I${STAGING_INCDIR}/ion_headers \
             -I${STAGING_INCDIR}/kernel-headers \
             -I${STAGING_INCDIR}/c++/ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS}"

EXTRA_OECMAKE += "-DAGL_LINUX:BOOL=ON"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

TOOLCHAIN = "clang"
