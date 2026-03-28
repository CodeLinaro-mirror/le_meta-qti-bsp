FILESEXTRAPATHS:prepend = "${WORKSPACE}/display/layers/meta-qti-display/recipes/drm/files:"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://0001-libdrm-Export-libdrm_macros.h-header.patch \
"

PACKAGECONFIG = "omap etnaviv install-test-programs nouveau tests"
