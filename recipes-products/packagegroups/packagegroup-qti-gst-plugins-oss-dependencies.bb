SUMMARY = "QTI Gstreamer OSS plugins dependencies package group"
LICENSE = "BSD-3-Clause-Clear"

PROVIDES = "${PACKAGES}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = " \
      packagegroup-qti-gst-plugins-oss-dependencies \
    "

RDEPENDS_packagegroup-qti-gst-plugins-oss-dependencies = " \
      linux-msm-headers \
      cairo-gobject \
      ffmpeg \
      gdk-pixbuf \
      jansson \
      json-glib \
      liba52 \
      libdaemon \
      libgudev \
      libmp3lame \
      librsvg \
      libsoup-2.4 \
      libtheora \
      libusb1 \
      libwebp \
      mpg123 \
      orc \
      readline \
      sbc \
      speex \
      taglib \
    "
