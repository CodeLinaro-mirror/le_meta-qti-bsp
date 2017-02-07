PR = "r0"

DEPENDS += "glib-2.0"

CFLAGS_append_pn-liblog = " -Dstrlcpy=g_strlcpy "
CFLAGS_append_pn-liblog = " -Dstrlcat=g_strlcat "
LDFLAGS_append_pn-liblog = " -lglib-2.0 -shared "

BBCLASSEXTEND = ""

do_install_append() {
   install -m 0644 -D ../50-log.rules ${D}${sysconfdir}/udev/rules.d/50-log.rules
}

FILES_${PN}-dbg    += "${libdir}/.debug/liblog.* ${bindir}/.debug/logcat"
FILES_${PN}        += "${libdir}/pkgconfig/* ${libdir}/liblog.so.* ${sysconfdir}/udev/rules.d/50-log.rules"
FILES_${PN}-dev    += "${libdir}/liblog.so ${libdir}/liblog.la ${includedir}/log ${includedir}/android"
FILES_${PN}-static += "${libdir}/liblog.a"
