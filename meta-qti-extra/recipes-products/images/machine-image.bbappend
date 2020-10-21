IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-qdrive', 'packagegroup-qti-qdrive', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-location', 'packagegroup-qti-location-hal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-lxc', 'packagegroup-qti-lxc', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'kdump-support', 'kexec-tools makedumpfile capture-image capture-devicetree', '', d)} \
    "

# Add libgomp support
IMAGE_INSTALL += "libgomp libgomp-dev libgomp-staticdev"

# Add kernel header to SDK.
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"

# Add kdump support
do_rootfs[depends] += "${@bb.utils.contains('DISTRO_FEATURES', 'kdump-support', 'machine-kdump-image:do_image_complete', '', d)}"
ROOTFS_POSTPROCESS_COMMAND_prepend = "${@bb.utils.contains('DISTRO_FEATURES', 'kdump-support', ' add_kdump_ramdisk; ', '', d)}"
add_kdump_ramdisk() {
    cp ${DEPLOY_DIR_IMAGE}/machine-kdump-image-${PRODUCT}.cpio.gz ${IMAGE_ROOTFS}/boot/kdump-ramdisk.cpio.gz
}
