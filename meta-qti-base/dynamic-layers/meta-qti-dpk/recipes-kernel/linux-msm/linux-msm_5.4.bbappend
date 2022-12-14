FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI:append = " \
    file://dpk.cfg \
    file://perf.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'variant-debug', 'file://dpk_debug-debug.cfg', 'file://dpk_debug-release.cfg', d)} \
    file://0003-Add-vendor-to-firmware-search-paths.patch;apply=no \
"
do_patch_more() {
    cd ${S}
    patch -f -p1 < ${WORKDIR}/0003-Add-vendor-to-firmware-search-paths.patch
}
addtask patch_more after do_patch before do_kernel_configme
