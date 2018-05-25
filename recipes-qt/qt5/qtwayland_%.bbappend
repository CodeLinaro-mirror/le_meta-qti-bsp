PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS_class-target += " gbm-headers adreno-native virtual/kernel glib-2.0 wayland gbm qtbase adreno-headers qtdeclarative wayland-native qtwayland-native"
DEPENDS_class-native += " adreno-native glib-2.0 wayland qtbase qtdeclarative wayland-native"
#rb1.4FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
#rb1.4
#rb1.4python do_getpatches() {
#rb1.4    import os
#rb1.4
#rb1.4    cmd = "mkdir -p ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtwayland && (wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-qt/qt5/qtwayland/0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtwayland/0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch || pwd)"
#rb1.4
#rb1.4    os.system(cmd)
#rb1.4}
#rb1.4
#rb1.4addtask getpatches before do_fetch
#rb1.4
#rb1.4SRC_URI_append = "\
#rb1.4    file://0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch \
#rb1.4    "
#rb1.4
