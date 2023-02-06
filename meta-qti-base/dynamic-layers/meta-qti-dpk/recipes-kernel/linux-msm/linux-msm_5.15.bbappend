FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI:append = " \
    file://dpk.cfg \
    file://perf.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'variant-debug', 'file://dpk_debug-debug.cfg', 'file://dpk_debug-release.cfg', d)} \
"
