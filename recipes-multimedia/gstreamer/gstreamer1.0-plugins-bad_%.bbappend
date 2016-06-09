FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
    file://0002-check-for-wayland-egl-1.0.0.patch \
    file://0003-libEGL-fix-missing-libs.patch \
    "

