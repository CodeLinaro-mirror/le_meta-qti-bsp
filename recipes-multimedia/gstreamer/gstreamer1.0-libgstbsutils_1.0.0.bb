inherit autotools pkgconfig

SUMMARY = "libgstbsutils for GStreamer"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=46ced35036593bf7372b800f47ed378a"

FILESPATH =+ "${WORKSPACE}/gstreamer/gst-sample:"
SRC_URI = "file://libgstbsutils"
SRC_URI += "git://anongit.freedesktop.org/gstreamer/common;destsuffix=libgstbsutils/common;branch=master;name=common"

S = "${WORKDIR}/libgstbsutils"

PR = "r1"
LV = "1.0.0"
SRCREV = "${AUTOREV}"

#conditional dependency on proprietary package
DEPENDS_append_prop = "gstreamer1.0-qtiutils"
