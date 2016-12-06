FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
    file://Fix_install_path_for_libs.patch \
    file://0001-enable-amb-key-plugin-and-correct-its-json-dependenc.patch \
    file://0002-correct-json-dependency-in-cangenplugin.patch \
    file://0003-modify-header-file-to-support-current-json-header-na.patch \
    file://0004-make-library-ambqt-support-setting-property-value.patch \
    file://0005-add-canplugin-to-support-self-defined-can-frame.patch \
    file://0006-support-all-demo-properties-and-fix-some-bugs.patch \
    file://0007-adjust-zone-mapping-policy-and-some-error-handling.patch \
"
do_install_append() {
cp -r  ${D}/usr/lib/* ${D}${libdir}
rm -rf ${D}/usr/lib
}
