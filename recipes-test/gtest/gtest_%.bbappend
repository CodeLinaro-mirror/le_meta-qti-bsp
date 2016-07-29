do_install_append() {
install -d ${D}${libdir}
cp -r  ${D}/usr/lib/* ${D}${libdir}
rm -rf ${D}/usr/lib
}


