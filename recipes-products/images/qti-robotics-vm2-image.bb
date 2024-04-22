inherit qimage qramdisk

DEPENDS += " virtual/kernel virtual/mkdtimg-native"

ENABLE_SECUREMSM = "${@d.getVar('MACHINE_SUPPORTS_SECUREMSM') or "True"}"

CORE_IMAGE_EXTRA_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'packagegroup-selinux-minimal', '', d)} \
    packagegroup-startup-scripts \
    packagegroup-filesystem-utils \
    vmsharememory-test \
"

#Exclude packages
PACKAGE_EXCLUDE += "readline"
ROOTFS_POSTPROCESS_COMMAND:remove = " do_fsconfig;"
USE_DEPMOD = "0"

do_gen_partition_bin[noexec] = "1"

IMAGE_FEATURES[validitems] += "vm oemvm"
IMAGE_FEATURES += "vm oemvm"
do_merge_dtbs[cleandirs] = "${DEPLOY_DIR_IMAGE}/kernel_dtbs/${OEMVM_IMAGE_ALIAS}-dtbs"
do_merge_dtbs() {

    cp -rp ${DEPLOY_DIR_IMAGE}/kernel_dtbs/kalamale_roboticsvm2*.dtb ${DEPLOY_DIR_IMAGE}/kernel_dtbs/${OEMVM_IMAGE_ALIAS}-dtbs/

    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform && \
    LD_LIBRARY_PATH=../../host/lib/:LD_LIBRARY_PATH \
    OUT_DIR=${KERNEL_PREBUILT_PATH} \
    BUILD_CONFIG=msm-kernel/build.config.msm.${VM_TARGET}.roboticsvm  \
    ./build/android/merge_dtbs.sh \
    ${DEPLOY_DIR_IMAGE}/kernel_dtbs/${OEMVM_IMAGE_ALIAS}-dtbs \
    ${DEPLOY_DIR_IMAGE}/build-artifacts/${OEMVM_IMAGE_ALIAS}-techpack-dtbos ${DEPLOY_DIR_IMAGE}/merged-${OEMVM_IMAGE_ALIAS}-dtbs/

    mkdtimg create ${DEPLOY_DIR_IMAGE}/${OEMVM_IMAGE_ALIAS}-${DTB_TARGET} --page_size=${PAGE_SIZE} \
    ${DEPLOY_DIR_IMAGE}/merged-${OEMVM_IMAGE_ALIAS}-dtbs/*.dtb
}

addtask do_merge_dtbs after do_makesystem before do_makeboot

do_compose_vmimage[recrdeptask] = "do_ramdisk_create"
do_compose_vmimage[recrdeptask] += "do_merge_dtbs"
do_compose_vmimage[recrdeptask] += "do_extracpio_create"
