FILESBBAPPENDPATH := "${THISDIR}"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BP}:${FILESBBAPPENDPATH}/${BPN}:"

SRC_URI:append = " \
    file://dpk.cfg \
    file://perf.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'variant-debug', 'file://dpk_debug-debug.cfg', 'file://dpk_debug-release.cfg', d)} \
    file://0003-Add-vendor-to-firmware-search-paths.patch;apply=no \
"

do_patch:append() {
    patch -d ${S} -p1 -i ${WORKDIR}/0003-Add-vendor-to-firmware-search-paths.patch
    if [ $? != 0 ];then
        bbfatal "patching 0003-Add-vendor-to-firmware-search-paths.patch failed"
    fi
}
