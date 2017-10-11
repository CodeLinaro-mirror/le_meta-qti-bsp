#package libs from correct libdir after adding mulitilib support.

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI = "git://github.com/GENIVI/${PN}.git;protocol=https \
          "
SRCREV = "44598504503eea5ac7f94c88477a5a78bda01f30"

python do_getpatches() {
    import os

    cmd = "mkdir -p ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/wayland-ivi-extension && (wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/wayland-ivi-extension/0001-wayland-ivi-extension-patch-for-wl-shell-emulator.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/wayland-ivi-extension/0001-wayland-ivi-extension-patch-for-wl-shell-emulator.patch || pwd)"
    os.system(cmd)
}

addtask getpatches before do_fetch


SRC_URI_append = "\
    file://0001-wayland-ivi-extension-patch-for-wl-shell-emulator.patch \
    "

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install_append() {
install -d ${D}${libdir}/
cp -r  ${D}/usr/lib/* ${D}${libdir}
rm -rf ${D}/usr/lib
}

FILES_${PN} += "${includedir}/*"
FILES_${PN} += "${libdir}/*.so*"
FILES_${PN}-dbg += "${libdir}/.debug/*"

INSANE_SKIP_${PN} += "dev-so"

FILES_${PN}-dev = ""

