IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-lxc', 'packagegroup-qti-lxc', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-location', 'packagegroup-qti-location-hal', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'kdump-support', 'kexec-tools makedumpfile capture-image capture-devicetree', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'packagegroup-selinux-minimal packagegroup-selinux-policycoreutils checkpolicy secilc auditd selinux-autorelabel', '', d)} \
    "

# Add libgomp support
IMAGE_INSTALL += "libgomp libgomp-dev libgomp-staticdev"

# Add kernel header to SDK.
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"

# Add kdump support
do_rootfs[depends] += "${@bb.utils.contains('MACHINE_FEATURES', 'kdump-support', 'initramfs-debug-image:do_image_complete', '', d)}"
ROOTFS_POSTPROCESS_COMMAND_prepend = "${@bb.utils.contains('MACHINE_FEATURES', 'kdump-support', ' add_kdump_ramdisk; ', '', d)}"
add_kdump_ramdisk() {
    cp ${DEPLOY_DIR_IMAGE}/initramfs-debug-image-${PRODUCT}.cpio.gz ${IMAGE_ROOTFS}/boot/capture-kernel-initramfs.cpio.gz
}
