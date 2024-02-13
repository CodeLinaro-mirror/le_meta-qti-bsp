inherit qimage qramdisk

DEPENDS += " virtual/kernel virtual/mkdtimg-native"

ENABLE_DISPLAY = "${@d.getVar('MACHINE_SUPPORTS_DISPLAY') or "True"}"
ENABLE_TOUCH = "${@d.getVar('MACHINE_SUPPORTS_TOUCH') or "True"}"
ENABLE_SECUREMSM = "${@d.getVar('MACHINE_SUPPORTS_SECUREMSM') or "True"}"

CORE_IMAGE_EXTRA_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'packagegroup-selinux-minimal', '', d)} \
    packagegroup-startup-scripts \
    packagegroup-filesystem-utils \
"

CORE_IMAGE_EXTRA_INSTALL += " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vm-persist', 'packagegroup-qti-encryption', '', d)}"

#Exclude packages
PACKAGE_EXCLUDE += "readline"
ROOTFS_POSTPROCESS_COMMAND:remove = " do_fsconfig;"
USE_DEPMOD = "0"

do_gen_partition_bin[noexec] = "1"

IMAGE_FEATURES[validitems] += "vm"
IMAGE_FEATURES += "vm"

do_merge_dtbs() {

    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform && \
    mkdir -p ${DEPLOY_DIR_IMAGE}/kernel_dtbs/vm-dtbs && \
    cp -rp ${DEPLOY_DIR_IMAGE}/kernel_dtbs/kalamale_roboticsvm1*.dtb ${DEPLOY_DIR_IMAGE}/kernel_dtbs/vm-dtbs/ && \
    LD_LIBRARY_PATH=../../host/lib/:LD_LIBRARY_PATH \
    OUT_DIR=${KERNEL_PREBUILT_PATH} \
    BUILD_CONFIG=msm-kernel/build.config.msm.${VM_TARGET}.roboticsvm  \
    ./build/android/merge_dtbs.sh \
    ${DEPLOY_DIR_IMAGE}/kernel_dtbs/vm-dtbs/ \
    ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos ${DEPLOY_DIR_IMAGE}/dtbs

    mkdtimg create ${DEPLOY_DIR_IMAGE}/trustedvm-${DTB_TARGET} --page_size=${PAGE_SIZE} \
    ${DEPLOY_DIR_IMAGE}/dtbs/*.dtb
}

addtask do_merge_dtbs after do_makesystem before do_makeboot

do_makeandsign_vmimage[depends] += "qti-robotics-vm2-image:do_image_complete"
do_compose_vmimage[recrdeptask] = "do_ramdisk_create"
do_compose_vmimage[recrdeptask] += "do_merge_dtbs"
do_compose_vmimage[recrdeptask] += "do_extracpio_create"
