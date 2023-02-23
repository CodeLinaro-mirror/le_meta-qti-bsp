SUMMARY = "QTI codec2 Plugin for GStreamer"
DESCRIPTION = "Gstreamer H/W decoder and encoder plugins based on codec2 APIs"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "LGPL-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://NOTICE;md5=e45e8fc7bdec198bec59831b0d7d16f7"

DEPENDS += "\
    codec2 \
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-base \
    libdrm \
    libxml2 \
    virtual/kernel-headers \
    media-codec2 \
    media-external \
    ${@oe.utils.version_less_or_equal('preferred-kernel', '5.14', '', 'videodlkm', d)} \
"

DEPENDS:append:lemans = " displaydlkm"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-codec2"

inherit meson pkgconfig

CFLAGS += "\
    -I${STAGING_INCDIR}/c++ \
    -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/vidc \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
"

CXXFLAGS += "\
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/vidc \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
"

CFLAGS:append:lemans = " -I${STAGING_INCDIR}/linux-msm/display"
CXXFLAGS:append:lemans = " -I${STAGING_INCDIR}/linux-msm/display"
EXTRA_OEMESON:append:lemans = " \
    -Dinterlace=disabled \
    -Dqprange=disabled \
    -Dir-cyclic=enabled \
    -Dmmmcolorfmt=true \
"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"

TOOLCHAIN = "clang"
