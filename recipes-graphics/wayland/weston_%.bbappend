FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
    file://weston.service_qc \
    file://weston.ini_qc \
    file://0001-outpub_fbdev-follow-the-work-flow-of-MSM8996.patch \
    file://0001-configure-don-t-control-egl-version.patch \
    file://weston-1.9-atomic-patches/0001-compositor-drm-Rename-drm_sprite-to-drm_plane.patch \
    file://weston-1.9-atomic-patches/0002-compositor-drm-Refactor-sprite-create-destroy-into-h.patch \
    file://weston-1.9-atomic-patches/0003-compositor-drm-Add-universal-plane-awareness.patch \
    file://weston-1.9-atomic-patches/0004-compositor-drm-Track-all-plane-types.patch \
    file://weston-1.9-atomic-patches/0005-compositor-drm-Track-cursor_plane-with-a-drm_plane.patch \
    file://weston-1.9-atomic-patches/0006-compositor-drm-Don-t-flip-planes-from-other-outputs.patch \
    file://weston-1.9-atomic-patches/0007-compositor-drm-Retain-DRM-FB-for-cursor-plane.patch \
    file://weston-1.9-atomic-patches/0008-compositor-drm-Track-primary-plane-with-a-drm_plane.patch \
    file://weston-1.9-atomic-patches/0009-compositor-drm-Atomic-modesetting-support.patch \
    file://weston-1.9-atomic-patches/0010-compositor-drm-Track-currently-active-view-for-plane.patch \
    file://weston-1.9-atomic-patches/0011-wip-scanout-plane.patch \
    file://weston-1.9-atomic-patches/0020-Enable-sprite-plane.patch \
    file://weston-1.9-atomic-patches/0021-compositor-drm-add-proper-stride-handle-for-NV12-UV-.patch \
    file://weston-1.9-atomic-patches/0022-compositor-drm-add-hw-cursor-support.patch \
    file://weston-1.9-atomic-patches/0023-drm-backend-fix-bug-overlay-plane-isn-t-disabled-whe.patch \
    "
CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"

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
}
