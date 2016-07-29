FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
    file://Fix_install_path_for_libs.patch \
"
do_install_append() {
cp -r  ${D}/usr/lib/* ${D}${libdir}
rm -rf ${D}/usr/lib
}
