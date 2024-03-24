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
    media-codec2 \
    media-external \
    videodlkm \
    virtual/kernel-headers \
"

# Enable AGL C2 service only on Makena, TODO: remove this when it's ready on Lemans
DEPENDS:append:quin-gvm-gen4-2 = " codec2-service"
EXTRA_OEMESON:append:quin-gvm-gen4-2 = " \
    -Dagl-c2service=true \
"

DEPENDS:append:lemans = " displaydlkm"
DEPENDS:append:quin-gvm-lemans = " displaydlkm"
DEPENDS:append:monaco = " displaydlkm"
DEPENDS:append:quin-gvm-monaco = " displaydlkm"

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

CFLAGS:append:lemans = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
CXXFLAGS:append:lemans = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
EXTRA_OEMESON:append:lemans = " \
    -Dc2dec-deinterlace=disabled \
    -Dqprange=disabled \
    -Dir-cyclic=enabled \
    -Dav1-dec=enabled \
    -Dmmmcolorfmt=true \
"

CFLAGS:append:quin-gvm-lemans = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
CXXFLAGS:append:quin-gvm-lemans = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
EXTRA_OEMESON:append:quin-gvm-lemans = " \
    -Dc2dec-deinterlace=disabled \
    -Dqprange=disabled \
    -Dir-cyclic=enabled \
    -Dav1-dec=enabled \
    -Dmmmcolorfmt=true \
"

CFLAGS:append:monaco = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
CXXFLAGS:append:monaco = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
EXTRA_OEMESON:append:monaco = " \
    -Dc2dec-deinterlace=disabled \
    -Dqprange=disabled \
    -Dir-cyclic=enabled \
    -Dmmmcolorfmt=true \
"

CFLAGS:append:quin-gvm-monaco = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
CXXFLAGS:append:quin-gvm-monaco = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
EXTRA_OEMESON:append:quin-gvm-monaco = " \
    -Dc2dec-deinterlace=disabled \
    -Dqprange=disabled \
    -Dir-cyclic=enabled \
    -Dmmmcolorfmt=true \
"

EXTRA_OEMESON += "\
    ${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', '', '-Dusedmaheap=true', d)} \
"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"

TOOLCHAIN = "clang"
