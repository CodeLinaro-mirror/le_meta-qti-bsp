PACKAGECONFIG[qt] = ""


DOTDEBUG-dbg += "/usr/lib/.debug "
FILES_${PN}-dev += "/usr/lib/libgpsd.so /usr/lib/libgps.so /usr/lib/pkgconfig"
FILES_libgpsd += "/usr/lib/libgpsd.so.*"
FILES_libgps += "/usr/lib/libgps.so.*"
