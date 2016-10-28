FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

PACKAGECONFIG[orc] = "--enable-orc,--disable-orc,orc"

SRC_URI_append += " \
   file://0001-gstreamer-Return-dropped-buffer-back-to-parent-class.patch \
"


