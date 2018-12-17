#package libs from correct libdir after adding mulitilib support.

SRC_URI = "git://github.com/GENIVI/${PN}.git;protocol=git \
          "

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = "\
     file://0001-ivi-controller-enable-ivi-share-function.patch \
     file://0001-ivi-controller-fix-return-value-error-for-set-up-buf.patch \
"
EXTRA_OECMAKE_remove = "-DIVI_SHARE=OFF"
EXTRA_OECMAKE = "-DIVI_SHARE=ON"

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

