DEFAULT_PREFERENCE = "-1"

require recipes-multimedia/gstreamer/gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://gstreamer/gstreamer"
SRC_URI += "git://anongit.freedesktop.org/gstreamer/common;destsuffix=gstreamer/common;branch=master;name=common"
SRCREV_common = "6f2d2093e84cc0eb99b634fa281822ebb9507285"
S = "${WORKDIR}/gstreamer"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

INSANE_SKIP_${PN} += "installed-vs-shipped"
