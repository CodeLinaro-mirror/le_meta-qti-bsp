FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:"
SRC_DIR = "${WORKSPACE}/graphics/weston/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
REPO_SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
S = "${WORKDIR}/weston"

python __anonymous () {

    # add early_init to DISTRO_FEATURES to use early user space feature
    if bb.utils.contains('DISTRO_FEATURES', 'early_init', True, False, d) or bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', True, False, d):
        d.appendVar("SRC_URI", " file://0001-weston-early-init-support.patch")

}

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
    file://weston.service_caf \
    file://weston.ini_caf \
    file://0001-outpub_fbdev-follow-the-work-flow-of-MSM8996.patch \
    file://0001-configure-don-t-control-egl-version.patch \
    file://drm_firmware_load_trigger.service \
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

EXTRA_OECONF_append = " --enable-ivi-shell"

CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"

# Remove community patch which is conflict with Weston SDM optimization
SRC_URI_remove = "file://0001-compositor-drm.c-Launch-without-input-devices.patch"
SRC_URI_remove = "file://fix-up-for-signal-11-on-qemux86.patch"
SRC_URI_remove = "file://weston.service"
SRC_URI_remove = "file://weston.ini"

#
# Weston strategy plugin
#
DEPENDS += "sdm scalar sdm-noship"

do_compile_prepend () {
    if [ -d "${S}/sdm_plugin" ]; then

        if [ -d "${WORKSPACE}/display-noship" ]; then
            export SCALAR_V1_HEADER_INC=${WORKSPACE}/display-noship/scalar/scalar_v1
            export SCALAR_QSEED3_HEADER_INC=${WORKSPACE}/display-noship/scalar/qseed3/inc
            export SDM_HEADER_INC=${WORKSPACE}/display-noship/sdm
        else
            export SCALAR_PREBUILT_DIR=${WORKSPACE}/prebuilt_${VARIANT}/${BASEMACHINE}/scalar
            export SDM_PREBUILT_DIR=${WORKSPACE}/prebuilt_${VARIANT}/${BASEMACHINE}/sdm-noship
            export SCALAR_V1_HEADER_INC=${SCALAR_PREBUILT_DIR}/usr/include
            export SCALAR_QSEED3_HEADER_INC=${SCALAR_PREBUILT_DIR}/usr/include
            export SDM_HEADER_INC=${SDM_PREBUILT_DIR}
            export DISPLAY_VERSION_HEADER_INC=${SDM_HEADER_INC}/usr/include
            export SCALAR_LIB_DIR=${SCALAR_PREBUILT_DIR}/usr/lib64
            export SDM_LIB_DIR=${SDM_PREBUILT_DIR}/usr/lib64
        fi

        # Use cmake compile sdm strategy plugin
        cd ${S}/sdm_plugin
        cmake -DCMAKE_INSTALL_LIBDIR=${STAGING_LIBDIR} -DCMAKE_CURRENT_SOURCE_DIR=${S}/sdm_plugin -DSYSROOT_LIBDIR=${STAGING_LIBDIR} -DSYSROOTINC_PATH=${STAGING_INCDIR} -DSCALAR_V1_HEADER_INC=${SCALAR_V1_HEADER_INC} -DSCALAR_QSEED3_HEADER_INC=${SCALAR_QSEED3_HEADER_INC} -DSDM_HEADER_INC=${SDM_HEADER_INC} -DSDM_PUBLIC_HEADER_INC=${STAGING_INCDIR}/sdm/include -DDISPLAY_VERSION_HEADER_INC=${DISPLAY_VERSION_HEADER_INC} -DSCALAR_LIB_DIR=${SCALAR_LIB_DIR} -DSDM_LIB_DIR=${SDM_LIB_DIR} .
        make

        # Install SDM strategy plugin header file for compiling
        install -d ${STAGING_INCDIR}
        install -d ${STAGING_INCDIR}/sdm_strategy_plugin
        install ${S}/sdm_plugin/sdm_strategy_plugin_interface.h ${STAGING_INCDIR}/sdm_strategy_plugin

        # Return to weston compile
        cd -
    fi
}

do_compile_append () {
    # Remove the temp SDM header file for compiling use, or else populate sysroot will fail
    if [ -d "${STAGING_INCDIR}/sdm_strategy_plugin" ]; then
        rm -rf ${STAGING_INCDIR}/sdm_strategy_plugin
    fi
}


#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor,--disable-drm-compositor,drm udev libgbm mtdev"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,libgbm"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "weston.service"

do_install_append() {
    # Install systemd unit files
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 644 -p -D ${WORKDIR}/weston.service_caf ${D}${systemd_system_unitdir}/weston.service
    fi

    WESTON_INI_CONFIG=${sysconfdir}/xdg/weston
    install -d ${D}${WESTON_INI_CONFIG}
    install -m 0644 ${WORKDIR}/weston.ini_caf ${D}${WESTON_INI_CONFIG}/weston.ini

    # Install SDM strategy plugin header file
    install -d ${D}${includedir}/
    install -d ${D}${includedir}/sdm_strategy_plugin
    install ${S}/sdm_plugin/sdm_strategy_plugin_interface.h ${D}${includedir}/sdm_strategy_plugin

    # Install SDM strategy plugin library
    install -d ${D}${libdir}/
    cp -r ${S}/sdm_plugin/libsdm_strategy_plugin.so ${D}${libdir}
    if ${@bb.utils.contains('BASEMACHINE', '8x96autofusion', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            install -d ${D}${systemd_unitdir}/system
            install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
            install -m 0644 ${WORKDIR}/drm_firmware_load_trigger.service ${D}${systemd_unitdir}/system/drm_firmware_load_trigger.service
            ln -sf ${systemd_unitdir}/system/drm_firmware_load_trigger.service ${D}${systemd_unitdir}/system/multi-user.target.wants/drm_firmware_load_trigger.service
        fi
    fi
}

pkg_postinst_${PN} () {
    if ${@bb.utils.contains('BASEMACHINE', '8x96autofusion', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            if [ -n "$D" ]; then
                OPTS="--root=$D"
            fi
            systemctl $OPTS mask weston.service
        fi
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)} || ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
        # Disable normal weston.service
        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            if [ -n "$D" ]; then
                OPTS="--root=$D"
            fi
            systemctl $OPTS mask weston.service
        fi
    fi
}

FILES_${PN} += "${includedir}/sdm_strategy_plugin/*"
FILES_${PN} += "${libdir}/libsdm_strategy_plugin.so"
FILES_${PN} += "${systemd_unitdir}/system/"
FILES_${PN} += "${sysconfdir}/xdg/weston/weston.ini"
FILES_SOLIBSDEV = ""
