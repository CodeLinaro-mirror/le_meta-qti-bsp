PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

python do_getpatches() {
    import os

    cmd = "mkdir -p ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtwayland && (wget https://git.codelinaro.org/clo/le/AGL/meta-agl-demo/-/raw/caf_migration/automotivelinux/chinook/recipes-qt/qt5/qtwayland/0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtwayland/0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch || pwd)"

    os.system(cmd)
}

addtask getpatches before do_fetch

SRC_URI_append = "\
    file://0001-Avoid-attaching-NULL-buffer-while-hiding-EGL-windows.patch \
    "

