DEFAULT_PREFERENCE = "-1"

require recipes-multimedia/gstreamer/gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://gstreamer/gst-plugins-ugly"
SRC_URI += "git://anongit.freedesktop.org/gstreamer/common;destsuffix=gst-plugins-ugly/common;branch=master;name=common"
SRCREV_common = "6f2d2093e84cc0eb99b634fa281822ebb9507285"
S = "${WORKDIR}/gst-plugins-ugly"

DEPENDS += "opencore-amr"

PACKAGECONFIG ??= "orc"

EXTRA_OECONF += " \
    --disable-a52dec \
    --disable-cdio \
    --disable-dvdlpcmdec \
    --disable-dvdread \
    --disable-dvdsub \
    --disable-lame \
    --disable-mad \
    --disable-mpeg2dec \
    --disable-mpg123 \
    --disable-realmedia \
    --disable-sidplay \
    --disable-twolame \
    --disable-x264 \
    --disable-xingmux \
    --disable-dependency-tracking \
    --disable-FEATURE \
    --enable-amrnb \
    --enable-amrwb \
    --enable-asfdemux \
    "

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

INSANE_SKIP_${PN} += "installed-vs-shipped"
