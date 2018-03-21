FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
# Get patches from meta-agl-demo chinook branch
python do_getpatches() {
    import os

    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0001-ivi-shell-fix-TODO-which-expects-only-one-screen-in-.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0001-ivi-shell-fix-TODO-which-expects-only-one-screen-in-.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0001-weston-patch-for-wl-shell-emulator.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0001-weston-patch-for-wl-shell-emulator.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0002-ivi-shell-avoid-inserting-a-ivi_layer-to-multiple-sc.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0002-ivi-shell-avoid-inserting-a-ivi_layer-to-multiple-sc.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0003-ivi-shell-fix-layout_layer.view_list-is-not-initiliz.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0003-ivi-shell-fix-layout_layer.view_list-is-not-initiliz.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0004-ivi-shell-remove-a-code-which-expects-only-a-screen-.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0004-ivi-shell-remove-a-code-which-expects-only-a-screen-.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0005-ivi-shell-multi-screen-support.-ivi_layout_screen-to.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0005-ivi-shell-multi-screen-support.-ivi_layout_screen-to.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0006-ivi-shell-transforming-from-a-single-screen-coordina.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0006-ivi-shell-transforming-from-a-single-screen-coordina.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/0007-RFR-ivi-shell-multi-screen-support-to-calcuration-of.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0007-RFR-ivi-shell-multi-screen-support-to-calcuration-of.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-agl/recipes-graphics/wayland/weston/fix-touchscreen-crash.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/fix-touchscreen-crash.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-ivi-common/recipes-graphics/wayland/weston-ivi-shell/0001-IVI-Shell-use-primary-screen-for-resolution.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-graphics/wayland/weston/0001-IVI-Shell-use-primary-screen-for-resolution.patch"
    os.system(cmd)
}
addtask getpatches before do_fetch

SRC_URI_append = "\
    file://fix-touchscreen-crash.patch \
"
SRC_URI_append = "\
    file://0001-weston-patch-for-wl-shell-emulator.patch \
    file://0001-ivi-shell-fix-TODO-which-expects-only-one-screen-in-.patch \
    file://0002-ivi-shell-avoid-inserting-a-ivi_layer-to-multiple-sc.patch \
    file://0003-ivi-shell-fix-layout_layer.view_list-is-not-initiliz.patch \
    file://0004-ivi-shell-remove-a-code-which-expects-only-a-screen-.patch \
    file://0005-ivi-shell-multi-screen-support.-ivi_layout_screen-to.patch \
    file://0006-ivi-shell-transforming-from-a-single-screen-coordina.patch \
    file://0007-RFR-ivi-shell-multi-screen-support-to-calcuration-of.patch \
" 
SRC_URI_append = " \
    file://0001-IVI-Shell-use-primary-screen-for-resolution.patch \
"

DEPENDS += "wayland-native gbm-headers"

TARGET_CFLAGS += "-lwayland-client"
