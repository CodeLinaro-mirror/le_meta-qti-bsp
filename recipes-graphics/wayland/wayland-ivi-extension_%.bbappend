#package libs from correct libdir after adding mulitilib support.

SRC_URI = "git://git.projects.genivi.org/${PN}.git;protocol=http \
          "

do_install_append() {
install -d ${D}${libdir}/
cp -r  ${D}/usr/lib/* ${D}${libdir}
rm -rf ${D}/usr/lib
}

FILES_${PN} += "${includedir}/*"
FILES_${PN} += "${libdir}/*.so*"
FILES_${PN}-dbg += "${libdir}/.debug/*"

INSANE_SKIP_${PN} += "dev-so"

FILES_${PN}-dev = ""

