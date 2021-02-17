DESCRIPTION = "QTI image capable of booting a device to the shell. The kernel includes \
the Minimal RAM-based Initial Root Filesystem (initramfs)"

LICENSE = "BSD-3-Clause"

addtask mkbootimage after do_rootfs before do_build

require ${QTI_METAPATH_BASE}/recipes-products/images/machine-image.bb
require include/qti-ramdisk.inc

do_rootfs_append() {
    bb.build.exec_func('do_ramdisk_create',d)
}

do_mkbootimage[depends] += "${PN}:do_rootfs"
do_mkbootimage[depends] += "virtual/kernel:do_deploy"
# We want to build updater everytime we build image
do_mkbootimage[nostamp] = "1"

do_mkbootimage() {
    # Make bootimage that boots to the ramdisk
    ${STAGING_BINDIR_NATIVE}/mkbootimg --kernel ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-dtb \
        --ramdisk ${DEPLOY_DIR_IMAGE}/${PN}-initrd.gz \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --pagesize ${PAGE_SIZE}  \
        --base ${KERNEL_BASE} \
        --tags-addr  ${KERNEL_TAGS_OFFSET} \
        --ramdisk_offset ${KERNEL_RAMDISK_OFFSET} \
        --output ${DEPLOY_DIR_IMAGE}/ramdisk-${BOOTIMAGE_TARGET}
}
