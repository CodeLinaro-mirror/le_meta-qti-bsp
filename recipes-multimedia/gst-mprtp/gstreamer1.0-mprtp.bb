DEFAULT_PREFERENCE = "-1"

HOMEPAGE    = "https://github.com/multipath-rtp/gst-mprtp"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2282fc857e14ce7b17c1c9d810504ac5"

DEPENDS += "gstreamer1.0"
DEPENDS += "gstreamer1.0-plugins-base"


FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://gstreamer/gst-mprtp"
SRCREV      = "${AUTOREV}"

SRC_DIR = "${WORKSPACE}/gstreamer/gst-mprtp"

S = "${WORKDIR}/gst-mprtp"

do_configure_prepend() {
    cd ${S}
    ./autogen.sh --noconfigure
    cd ${B}
}

do_install() {
   install ${S}/plugins/.libs/libgstmprtp.so  ${D}/${libdir}/gstreamer-1.0/
}

FILES_${PN} =+ "${libdir}/gstreamer-mprtp/*"
FILES_${PN}-dbg += "${libdir}/gstreamer-mprtp/.debug/*"

INSANE_SKIP_${PN} += " useless-rpaths"

