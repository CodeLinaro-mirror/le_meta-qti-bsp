DEFAULT_PREFERENCE = "-1"

DEPENDS += "opencore-amr"

PACKAGECONFIG ??= "orc opencore-amr"

EXTRA_OEMESON += " \
    -Da52dec=disabled \
    -Dcdio=disabled \
    -Ddvdlpcmdec=disabled \
    -Ddvdread=disabled \
    -Ddvdsub=disabled \
    -Dmpeg2dec=disabled \
    -Drealmedia=disabled \
    -Dsidplay=disabled \
    -Dx264=disabled \
    -Dxingmux=disabled \
    -Damrnb=enabled \
    -Damrwbdec=enabled \
    -Dasfdemux=enabled \
    "

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

