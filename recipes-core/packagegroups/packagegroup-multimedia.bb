
SUMMARY = "multimedia framework"
DESCRIPTION = "packages for multimedia"
LICENSE = "GPLv2+ & LGPLv2+"

inherit packagegroup

ALLOW_EMPTY_${PN} = "1"
PACKAGES = "${PN}"

RDEPENDS_${PN} = " \
        gstreamer1.0 \
        gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good \
        gstreamer1.0-plugins-bad \
        gstreamer1.0-libav \
        gstreamer1.0-omx \
        gstreamer1.0-plugins-ugly \
        gdk-pixbuf-loader-bmp \
        gdk-pixbuf-loader-gif \
        gdk-pixbuf-loader-tiff \
        alsa-lib \
        alsa-utils \
        alsa-plugins \
"
#rb1.4        gdk-pixbuf-loader-bmp 
#rb1.4        gdk-pixbuf-loader-gif 
#rb1.4        gdk-pixbuf-loader-tiff 

RDEPENDS_${PN}_remove_8x96autodvrs += "alsa-lib alsa-utils alsa-plugins"
# Disable gstreamer1.0-omx until solve media build issue.
RDEPENDS_${PN}_remove_8x96autodvrs += "gstreamer1.0-omx"
RDEPENDS_${PN}_remove_8x96auto += "alsa-lib"
RDEPENDS_${PN}_remove_8x96autogvmgh += "alsa-lib"
RDEPENDS_${PN}_remove_8x96autogvmga += "alsa-lib"

RDEPENDS_${PN}_append_8x96autodvrs += "gstreamer1.0-qvblend gstreamer1.0-plugins-qscreencapsrc"
RDEPENDS_${PN}_append_8x96auto += "gstreamer1.0-qvblend gstreamer1.0-plugins-qscreencapsrc"
