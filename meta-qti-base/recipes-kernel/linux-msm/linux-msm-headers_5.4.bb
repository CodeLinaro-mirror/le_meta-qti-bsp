SUMMARY = "MSM Linux Kernel Headers"
DESCRIPTION = "Installs MSM kernel headers required to build userspace. \
These headers are installed in ${includedir}/linux-msm path."
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "kernel"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "bison-native rsync-native unifdef-native virtual/kernel"

S = "${STAGING_KERNEL_DIR}"
B = "${WORKDIR}/build"

inherit linux-kernel-base kernel-arch

# We need the kernel to be unpacked and patched before we can grab the headers.
do_install[depends] += "virtual/kernel:do_patch"

# There's nothing to do here, except install the headers where we can package them
do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

COMMON_UAPI_HEADERS = " \
    asm-generic/ioctls.h \
    linux/ion.h \
    linux/bsg.h \
    linux/capability.h \
    linux/stddef.h \
    linux/fs.h \
    linux/fscrypt.h \
    linux/limits.h \
    linux/mount.h \
    linux/types.h \
    linux/dma-buf.h \
    linux/string.h \
    linux/v4l2-controls.h \
    drm/drm_fourcc.h \
    drm/drm.h \
    drm/drm_mode.h \
    drm/msm_drm.h \
"

MSM_UAPI_HEADERS = " \
    linux/rmnet_data.h \
    linux/msm_rmnet.h \
    linux/msm_ion.h \
    linux/msm_ion_ids.h \
    linux/msm_npu.h \
    linux/msm_kgsl.h \
    linux/qseecom.h \
    linux/qcedev.h \
    linux/fips_status.h \
    linux/smcinvoke.h \
    linux/habmmid.h \
    linux/hab_ioctl.h \
    linux/hgsl.h \
"

DISPLAY_UAPI_HEADERS = " \
    drm/msm_drm_pp.h \
    drm/sde_drm.h \
    linux/mdss_rotator.h \
    linux/msm_mdp_ext.h \
    linux/msm_mdp.h \
    media/msm_media_info.h \
    media/msm_sde_rotator.h \
    video/msm_hdmi_hdcp_mgr.h \
    video/msm_hdmi_modes.h \
"

AIS_UAPI_HEADERS = " \
    media/ais_isp.h \
    media/cam_cpas.h \
    media/cam_icp.h \
    media/cam_isp_vfe.h \
    media/cam_req_mgr.h \
    media/ais_sensor.h \
    media/cam_defs.h \
    media/cam_isp.h \
    media/cam_jpeg.h \
    media/cam_sensor.h \
    media/msm_media_info.h \
    media/ais_v4l2loopback.h \
    media/cam_fd.h \
    media/cam_isp_ife.h \
    media/cam_lrme.h \
    media/cam_sync.h \
"

VIDEO_UAPI_HEADERS = " \
   media/msm_media_info.h \
   media/msm_vidc_private.h \
   media/msm_vidc_utils.h \
"

do_install() {
    HEADER_INSTALL_TOOL=${STAGING_KERNEL_DIR}/scripts/headers_install.sh

    # copy version.h
    install -d ${D}${includedir}/linux-msm/linux
    ${HEADER_INSTALL_TOOL} ${STAGING_KERNEL_BUILDDIR}/include/generated/uapi/linux/version.h ${D}${includedir}/linux-msm/linux/

    cd ${STAGING_KERNEL_BUILDDIR}
    install -d ${D}${includedir}/linux-msm/drm
    install -d ${D}${includedir}/linux-msm/asm-generic
    for h in ${COMMON_UAPI_HEADERS}; do
        ${HEADER_INSTALL_TOOL} ${STAGING_KERNEL_DIR}/include/uapi/$h ${D}${includedir}/linux-msm/$h
    done
    for h in ${MSM_UAPI_HEADERS}; do
        ${HEADER_INSTALL_TOOL} ${STAGING_KERNEL_DIR}/include/uapi/$h ${D}${includedir}/linux-msm/$h
    done

    install -d ${D}${includedir}/linux-msm/display
    install -d ${D}${includedir}/linux-msm/display/drm
    install -d ${D}${includedir}/linux-msm/display/linux
    install -d ${D}${includedir}/linux-msm/display/media
    install -d ${D}${includedir}/linux-msm/display/video

    for h in ${DISPLAY_UAPI_HEADERS}; do
        ${HEADER_INSTALL_TOOL} ${STAGING_KERNEL_DIR}/techpack/display/include/uapi/display/$h ${D}${includedir}/linux-msm/display/$h
    done

    install -d ${D}${includedir}/linux-msm/ais
    install -d ${D}${includedir}/linux-msm/ais/media
    for h in ${AIS_UAPI_HEADERS}; do
        ${HEADER_INSTALL_TOOL} ${STAGING_KERNEL_DIR}/techpack/ais/include/uapi/ais/$h ${D}${includedir}/linux-msm/ais/$h
    done

    install -d ${D}${includedir}/linux-msm/vidc
    install -d ${D}${includedir}/linux-msm/vidc/media
    for h in ${VIDEO_UAPI_HEADERS}; do
        ${HEADER_INSTALL_TOOL} ${STAGING_KERNEL_DIR}/techpack/video/include/uapi/vidc/$h ${D}${includedir}/linux-msm/vidc/$h
    done

    # Need to create a hierarchy that works for Adreno's expectation that
    # its KERN_INCDIR variable is pointed at a directory with usr/include
    # below it with the headers.  Special casing for Adreno as opposed to
    # having to use ${STAGING_INCDIR}/X/usr/include everywhere seems like
    # a cleaner approach, and this could be removed if the Adreno makefiles
    # can be changed to just use KERN_INCDIR directly.
    # A separate hierarchy as opposed to one under ${D}/usr/include/linux-msm is
    # used to avoid issues from a symlink loop.
    install -d ${D}${includedir}/kernel/usr
    ln -sf ../../linux-msm ${D}${includedir}/kernel/usr/include
}

RDEPENDS:${PN}-dev = ""

PACKAGE_ARCH = "${MACHINE_ARCH}"
