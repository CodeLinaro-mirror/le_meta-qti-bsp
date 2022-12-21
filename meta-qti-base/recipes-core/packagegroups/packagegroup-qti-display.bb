SUMMARY = "QTI package group for weston"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-display \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "displaydlkm", "", d)} \
    libdrm \
    wayland \
    wayland-ivi-extension \
    weston \
    weston-init \
    weston-examples \
    display-hal-linux \
    display-commonsys-intf-linux \
    ${@bb.utils.contains('LAYERSERIES_COMPAT_yocto', 'dunfell', '', 'weston-sdm-extension', d)} \
    "

RDEPENDS:${PN}:remove:qti-dpk = "wayland-ivi-extension"
RDEPENDS:${PN}:append:qti-dpk = " weston-udev"
