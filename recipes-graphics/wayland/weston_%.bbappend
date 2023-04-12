
SOURCE_WESTON_PATCHES = "https://git.codelinaro.org/clo/le/AGL/meta-agl-demo/-/raw/automotivelinux/chinook/recipes-graphics/wayland/weston/"

#rb1.4 #need to add into SRC_URI_append
#rb1.4    # add early_init to DISTRO_FEATURES to use early user space feature
#rb1.4    if bb.utils.contains('DISTRO_FEATURES', 'early_init', True, False, d) or bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', True, False, d):
#rb1.4        d.appendVar("SRC_URI", " file://0001-weston-early-init-support.patch")
#rb1.4
#rb1.4}
#rb1.4
#rb1.4FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
#rb1.4
#rb1.4# Get patches from meta-agl-demo chinook branch
#rb1.4python do_getpatches() {
#rb1.4    import os
#rb1.4
#rb1.4SOURCE_WESTON_PATCHES = "https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-graphics/wayland/weston/"
#rb1.4
#rb1.4SRC_URI_append = "\
#rb1.4    file://weston.service_caf \
#rb1.4    file://weston.ini_caf \
#rb1.4    file://0001-outpub_fbdev-follow-the-work-flow-of-MSM8996.patch \
#rb1.4    file://0001-configure-don-t-control-egl-version.patch \
#rb1.4    file://drm_firmware_load_trigger.service 
SRC_URI_append = "\
    https://git.codelinaro.org/clo/le/AGL/meta-agl/-/raw/automotivelinux/chinook/meta-agl/recipes-graphics/wayland/weston/fix-touchscreen-crash.patch;downloadfilename=fix-touchscreen-crash.patch;md5sum=62798230b8bb88f00ee43247fef61713;sha256sum=dd25f196cbe7e8b1ca59ec2b16e7f73dd43995c72ce2175447e3787b98635b28 \
"
SRC_URI_append = "\
    ${SOURCE_WESTON_PATCHES}/0001-weston-patch-for-wl-shell-emulator.patch;downloadfilename=0001-weston-patch-for-wl-shell-emulator.patch;md5sum=ab4bbc2ec8d5eee375b6b8e5edcb203f;sha256sum=c44d787aa8fabf4f60ab4bf6c0f24cdc3817fbe763f384cf223b7979b44c77f0 \
    ${SOURCE_WESTON_PATCHES}/0001-ivi-shell-fix-TODO-which-expects-only-one-screen-in-.patch;downloadfilename=0001-ivi-shell-fix-TODO-which-expects-only-one-screen-in-.patch;md5sum=b243e514098fa6978dd4c7e6080f3351;sha256sum=5791aee2ec7b408755d77c5ac01a882360c60fcafb69495f90acd0600efa74da \
    ${SOURCE_WESTON_PATCHES}/0002-ivi-shell-avoid-inserting-a-ivi_layer-to-multiple-sc.patch;downloadfilename=0002-ivi-shell-avoid-inserting-a-ivi_layer-to-multiple-sc.patch;md5sum=390ef0d6ad7e34ff00e63883498e132a;sha256sum=c7e4adf7a5aadedb087cbd3704af9c9b0c8036d3a3b644d0076c53208e89cb22 \
    ${SOURCE_WESTON_PATCHES}/0003-ivi-shell-fix-layout_layer.view_list-is-not-initiliz.patch;downloadfilename=0003-ivi-shell-fix-layout_layer.view_list-is-not-initiliz.patch;md5sum=f58ae6cb9100373a61a1f0d4e75c20d5;sha256sum=b0bb7d4c1bc701446ad631dc40f58fe4b4463c0c9f6360f5957578c24384a673 \
    ${SOURCE_WESTON_PATCHES}/0004-ivi-shell-remove-a-code-which-expects-only-a-screen-.patch;downloadfilename=0004-ivi-shell-remove-a-code-which-expects-only-a-screen-.patch;md5sum=e13439a08fe622d7e605fd80880683c8;sha256sum=50243cbd9cfcfcf6365472ecb760ebfbd9a497c7f82b4d8a01fd961d750809f5 \
    ${SOURCE_WESTON_PATCHES}/0005-ivi-shell-multi-screen-support.-ivi_layout_screen-to.patch;downloadfilename=0005-ivi-shell-multi-screen-support.-ivi_layout_screen-to.patch;md5sum=a82e3e17a569e9da55f2fc450b9aa224;sha256sum=6d3295a29eda5bbe05409b251f3d60650d0ade5137e0e618b17bf236a443618a \
    ${SOURCE_WESTON_PATCHES}/0006-ivi-shell-transforming-from-a-single-screen-coordina.patch;downloadfilename=0006-ivi-shell-transforming-from-a-single-screen-coordina.patch;md5sum=7e29fbe0b9715ae56dd82b582f2e044e;sha256sum=a055d40ea563566b4e9e467d6021521a21c1e5ae13e3e595a4624a53d76f4bc9 \
    ${SOURCE_WESTON_PATCHES}/0007-RFR-ivi-shell-multi-screen-support-to-calcuration-of.patch;downloadfilename=0007-RFR-ivi-shell-multi-screen-support-to-calcuration-of.patch;md5sum=04db444670948332220fc70e9fd7d9c8;sha256sum=2924b27224529d065d543c0f396ee9c32b061bd65baf5b82ad80ab12ca4aafea \
" 
SRC_URI_append = " \
    https://git.codelinaro.org/clo/le/AGL/meta-agl/-/raw/automotivelinux/chinook/meta-ivi-common/recipes-graphics/wayland/weston-ivi-shell/0001-IVI-Shell-use-primary-screen-for-resolution.patch;downloadfilename=0001-IVI-Shell-use-primary-screen-for-resolution.patch;md5sum=3bc2dc2cec11ffcaa26f71ad44f34a88;sha256sum=7d35301488c1bb94871a04c4bf746da8756ea2f4d488a6aa248e3199695341c7 \
"

DEPENDS += "wayland-native gbm-headers"

TARGET_CFLAGS += "-lwayland-client"
#rb1.4EXTRA_OECONF_append = " --enable-ivi-shell"
#rb1.4
#rb1.4CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"
#rb1.4
#rb1.4# Remove community patch which is conflict with Weston SDM optimization
#rb1.4SRC_URI_remove = "file://0001-compositor-drm.c-Launch-without-input-devices.patch"
#rb1.4SRC_URI_remove = "file://fix-up-for-signal-11-on-qemux86.patch"
#rb1.4SRC_URI_remove = "file://weston.service"
#rb1.4SRC_URI_remove = "file://weston.ini"
#rb1.4
#rb1.4#
#rb1.4# Weston strategy plugin
#rb1.4#
#rb1.4DEPENDS += "sdm scalar sdm-noship  cmake-native wayland-native"
#rb1.4
#rb1.4do_compile_prepend () {
#rb1.4    if [ -d "${S}/sdm_plugin" ]; then
#rb1.4
#rb1.4        if [ -d "${WORKSPACE}/display-noship" ]; then
#rb1.4            export SCALAR_V1_HEADER_INC=${WORKSPACE}/display-noship/scalar/scalar_v1
#rb1.4            export SCALAR_QSEED3_HEADER_INC=${WORKSPACE}/display-noship/scalar/qseed3/inc
#rb1.4            export SDM_HEADER_INC=${WORKSPACE}/display-noship/sdm
#rb1.4        else
#rb1.4            export SCALAR_PREBUILT_DIR=${WORKSPACE}/prebuilt_${VARIANT}/${BASEMACHINE}/scalar
#rb1.4            export SDM_PREBUILT_DIR=${WORKSPACE}/prebuilt_${VARIANT}/${BASEMACHINE}/sdm-noship
#rb1.4            export SCALAR_V1_HEADER_INC=${SCALAR_PREBUILT_DIR}/usr/include
#rb1.4            export SCALAR_QSEED3_HEADER_INC=${SCALAR_PREBUILT_DIR}/usr/include
#rb1.4            export SDM_HEADER_INC=${SDM_PREBUILT_DIR}
#rb1.4            export DISPLAY_VERSION_HEADER_INC=${SDM_HEADER_INC}/usr/include
#rb1.4            export SCALAR_LIB_DIR=${SCALAR_PREBUILT_DIR}/usr/lib64
#rb1.4            export SDM_LIB_DIR=${SDM_PREBUILT_DIR}/usr/lib64
#rb1.4        fi
#rb1.4
#rb1.4        # Use cmake compile sdm strategy plugin
#rb1.4        cd ${S}/sdm_plugin
#rb1.4        cmake -DCMAKE_INSTALL_LIBDIR=${STAGING_LIBDIR} -DCMAKE_CURRENT_SOURCE_DIR=${S}/sdm_plugin -DSYSROOT_LIBDIR=${STAGING_LIBDIR} -DSYSROOTINC_PATH=${STAGING_INCDIR} -DSCALAR_V1_HEADER_INC=${SCALAR_V1_HEADER_INC} -DSCALAR_QSEED3_HEADER_INC=${SCALAR_QSEED3_HEADER_INC} -DSDM_HEADER_INC=${SDM_HEADER_INC} -DSDM_PUBLIC_HEADER_INC=${STAGING_INCDIR}/sdm/include -DDISPLAY_VERSION_HEADER_INC=${DISPLAY_VERSION_HEADER_INC} -DSCALAR_LIB_DIR=${SCALAR_LIB_DIR} -DSDM_LIB_DIR=${SDM_LIB_DIR} .
#rb1.4        make
#rb1.4
#rb1.4        # Install SDM strategy plugin header file for compiling
#rb1.4        install -d ${STAGING_INCDIR}
#rb1.4        install -d ${STAGING_INCDIR}/sdm_strategy_plugin
#rb1.4        install ${S}/sdm_plugin/sdm_strategy_plugin_interface.h ${STAGING_INCDIR}/sdm_strategy_plugin
#rb1.4
#rb1.4        # Return to weston compile
#rb1.4        cd -
#rb1.4    fi
#rb1.4}
#rb1.4
#rb1.4do_compile_append () {
#rb1.4    # Remove the temp SDM header file for compiling use, or else populate sysroot will fail
#rb1.4    if [ -d "${STAGING_INCDIR}/sdm_strategy_plugin" ]; then
#rb1.4        rm -rf ${STAGING_INCDIR}/sdm_strategy_plugin
#rb1.4    fi
#rb1.4}
#rb1.4
#rb1.4
#rb1.4#
#rb1.4# Compositor choices
#rb1.4#
#rb1.4# Weston on KMS
#rb1.4PACKAGECONFIG[kms] = "--enable-drm-compositor,--disable-drm-compositor,drm udev libgbm mtdev"
#rb1.4# Weston on Wayland (nested Weston)
#rb1.4PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,libgbm"
#rb1.4
#rb1.4inherit systemd
#rb1.4
#rb1.4SYSTEMD_PACKAGES = "${PN}"
#rb1.4SYSTEMD_SERVICE_${PN} = "weston.service"
#rb1.4
#rb1.4do_install_append() {
#rb1.4    # Install systemd unit files
#rb1.4    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
#rb1.4        install -m 644 -p -D ${WORKDIR}/weston.service_caf ${D}${systemd_system_unitdir}/weston.service
#rb1.4    fi
#rb1.4
#rb1.4    WESTON_INI_CONFIG=${sysconfdir}/xdg/weston
#rb1.4    install -d ${D}${WESTON_INI_CONFIG}
#rb1.4    install -m 0644 ${WORKDIR}/weston.ini_caf ${D}${WESTON_INI_CONFIG}/weston.ini
#rb1.4
#rb1.4    # Install SDM strategy plugin header file
#rb1.4    install -d ${D}${includedir}/
#rb1.4    install -d ${D}${includedir}/sdm_strategy_plugin
#rb1.4    install ${S}/sdm_plugin/sdm_strategy_plugin_interface.h ${D}${includedir}/sdm_strategy_plugin
#rb1.4
#rb1.4    # Install SDM strategy plugin library
#rb1.4    install -d ${D}${libdir}/
#rb1.4    cp -r ${S}/sdm_plugin/libsdm_strategy_plugin.so ${D}${libdir}
#rb1.4    if ${@bb.utils.contains('BASEMACHINE', '8x96autofusion', 'true', 'false', d)}; then
#rb1.4        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
#rb1.4            install -d ${D}${systemd_unitdir}/system
#rb1.4            install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
#rb1.4            install -m 0644 ${WORKDIR}/drm_firmware_load_trigger.service ${D}${systemd_unitdir}/system/drm_firmware_load_trigger.service
#rb1.4            ln -sf ${systemd_unitdir}/system/drm_firmware_load_trigger.service ${D}${systemd_unitdir}/system/multi-user.target.wants/drm_firmware_load_trigger.service
#rb1.4        fi
#rb1.4    fi
#rb1.4}
#rb1.4
#rb1.4pkg_postinst_${PN} () {
#rb1.4    if ${@bb.utils.contains('BASEMACHINE', '8x96autofusion', 'true', 'false', d)}; then
#rb1.4        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
#rb1.4            if [ -n "$D" ]; then
#rb1.4                OPTS="--root=$D"
#rb1.4            fi
#rb1.4            systemctl $OPTS mask weston.service
#rb1.4        fi
#rb1.4    fi
#rb1.4}
#rb1.4
#rb1.4FILES_${PN} += "${includedir}/sdm_strategy_plugin/*"
#rb1.4FILES_${PN} += "${libdir}/libsdm_strategy_plugin.so"
#rb1.4FILES_${PN} += "${systemd_unitdir}/system/"
#rb1.4FILES_${PN} += "${sysconfdir}/xdg/weston/weston.ini"
#rb1.4FILES_SOLIBSDEV = ""
