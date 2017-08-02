SUMMARY = "Test application for GStreamer"
SECTION = "multimedia"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://gstreamer/gst-sample/multisend"

S = "${WORKDIR}/gstreamer/gst-sample/multisend"

PR = "r1"

DEPENDS = "gstreamer1.0"

# Need the kernel headers
#PACKAGE_ARCH = "${MACHINE_ARCH}"

LV = "1.0.0"

inherit autotools pkgconfig gettext
