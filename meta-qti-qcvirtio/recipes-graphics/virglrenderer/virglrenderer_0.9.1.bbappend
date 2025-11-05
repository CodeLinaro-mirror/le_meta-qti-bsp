FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# virtual/libgbm dependency is not sufficient on QTI platform
DEPENDS:append:class-target = " gbm-headers udev glib-2.0 wayland "

SRC_URI += "\
    file://0001-include-missing-glgeterror-in-format-check.patch \
    file://0002-vrend-define-missing-GBM_FORMAT_R8-format.patch \
    file://0003-workaround-for-ucompare-shader-compiler-bug.patch \
    file://0004-workaround-disable-gltextureview.patch \
    file://0005-workaround-disable-dual-src-blend.patch \
"
