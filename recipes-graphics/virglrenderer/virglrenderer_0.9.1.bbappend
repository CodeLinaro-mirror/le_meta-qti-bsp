FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEPENDS = "libdrm virtual/libgles2 virtual/libgbm libepoxy"
DEPENDS:remove:class-native = "virtual/libgles2"
DEPENDS:append:class-native = " virtual/libgl"
DEPENDS:append:class-target = " udev glib-2.0 wayland "

SRC_URI += " \
    file://0001-include-missing-glgeterror-in-format-check.patch \
    file://0001-vrend-define-missing-GBM_FORMAT_R8-format.patch \
    file://0002-workaround-for-ucompare-shader-compiler-bug.patch \
    file://0003-workaround-disable-gltextureview.patch \
    file://0004-workaround-disable-dual-src-blend.patch \
"

REQUIRED_DISTRO_FEATURES:class-native = ""
REQUIRED_DISTRO_FEATURES:class-nativesdk = ""