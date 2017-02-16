FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:"
SRC_DIR = "${WORKSPACE}/graphics/weston/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
REPO_SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
S = "${WORKDIR}/weston"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
SRC_URI_append = "\
    file://weston.service_qc \
    file://weston.ini_qc \
    file://0001-outpub_fbdev-follow-the-work-flow-of-MSM8996.patch \
    file://0001-configure-don-t-control-egl-version.patch \
"
CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"


#
# Weston strategy plugin
#
DEPENDS += "sdm scalar sdm-noship"

EXTRA_OECMAKE += "-DSDM_PUBLIC_HEADER_INC:STRING=${STAGING_INCDIR}/sdm/include"
EXTRA_OECMAKE += "-DSDM_PROPRIETARY_HEADER_INC:STRING=${WORKSPACE}/display-noship/sdm"
EXTRA_OECMAKE += "-DSCALAR_HEADER_INC:STRING=${WORKSPACE}/display-noship/scalar"
EXTRA_OECMAKE += "-DSYSROOTINC_PATH:STRING=${STAGING_INCDIR}"
EXTRA_OECMAKE += "-DCMAKE_CURRENT_SOURCE_DIR:STRING=${S}/sdm_plugin"
EXTRA_OECMAKE += "-DSYSROOT_LIBDIR:STRING=${D}"

do_compile_prepend () {
    if [ -d "${S}/sdm_plugin" ]; then
        # Use cmake compile sdm strategy plugin
        cd ${S}/sdm_plugin
        cmake -DCMAKE_INSTALL_LIBDIR=${STAGING_LIBDIR} -DCMAKE_CURRENT_SOURCE_DIR=${S}/sdm_plugin -DSYSROOT_LIBDIR=${STAGING_INCDIR} -DSYSROOTINC_PATH=${STAGING_INCDIR} -DSCALAR_HEADER_INC=${WORKSPACE}/display-noship/scalar -DSDM_PROPRIETARY_HEADER_INC=${WORKSPACE}/display-noship/sdm -DSDM_PUBLIC_HEADER_INC=${STAGING_INCDIR}/sdm/include .
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

do_install_append() {
    # Install systemd unit files
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 644 -p -D ${WORKDIR}/weston.service_qc ${D}${systemd_system_unitdir}/weston.service
    fi

    install -m 0644 ${WORKDIR}/weston.ini_qc ${D}${WESTON_INI_CONFIG}/weston.ini

    # Install SDM strategy plugin header file
    install -d ${D}${includedir}/
    install -d ${D}${includedir}/sdm_strategy_plugin
    install ${S}/sdm_plugin/sdm_strategy_plugin_interface.h ${D}${includedir}/sdm_strategy_plugin

    # Install SDM strategy plugin library
    install -d ${D}${libdir}/
    cp -r ${S}/sdm_plugin/libsdm_strategy_plugin.so ${D}${libdir}
}

FILES_${PN} += "${includedir}/sdm_strategy_plugin/*"
FILES_${PN} += "${libdir}/libsdm_strategy_plugin.so"
FILES_SOLIBSDEV = ""
