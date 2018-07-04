PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS_class-target += " gbm-headers adreno-native virtual/kernel glib-2.0 wayland gbm qtbase adreno-headers qtdeclarative wayland-native qtwayland-native"
DEPENDS_class-native += " adreno-native glib-2.0 wayland qtbase qtdeclarative wayland-native"

#rb1.4 SRC_URI_append = "\
#rb1.4    https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-qt/qt5/qtwayland/0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch?h=automotivelinux/chinook;downloadfilename=0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch;md5sum=a7181cd28d4cd6c04b6390c34ed364a2;sha256sum=04535b134ab199e94d516f9345619f92d9d65db1f8db9e7de0b6b48673c2ead0 \
#rb1.4    "
