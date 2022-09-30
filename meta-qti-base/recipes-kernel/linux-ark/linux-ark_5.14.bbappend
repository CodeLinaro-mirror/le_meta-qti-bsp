MY_SRC = "${SRC_DIR_ROOT}/kernel/rh-kernel-5.14"
PATCH_DIR = "${SRC_DIR_ROOT}/meta-qti-bsp/meta-qti-base/recipes-kernel/linux-ark/files/"
MY_WDIR = "${WORKDIR}/kernel/rh-kernel-5.14"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-centos-5.14-Fix-to-bypass-redhad-env.patch \
            file://0002-centos-5.14-build-fixes-while-porting-from-5.4.patch \
            file://0001-defconfig-add-overrides-to-resolve-build-error.patch \
            file://0001-redhat-HACK-remove-rpm-build-dependency.patch \
"
do_rh_config () {
    make -C ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/redhat  ARCH=arm64 dist-configs
    cp ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/redhat/configs/kernel-automotive-5.14.0-aarch64.config ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/arch/arm64/configs/defconfig
    make -C ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14 CROSS_COMPILE="" defconfig
    make -C ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14 CROSS_COMPILE="" savedefconfig
    cp ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/defconfig ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/arch/arm64/configs/defconfig
    cp ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/defconfig ${SRC_DIR_ROOT}/meta-qti-bsp/meta-qti-base/recipes-kernel/linux-ark/files/defconfig
    rm -rf ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/.config ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/include/config/ \
    ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/include/generated/ ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/arch/$ARCH/include/generated/
}

addtask rh_config after do_fetch before do_unpack

do_patch_config() {
     do_patch_config_call() {
         cd ${MY_SRC}
         patch -f -p1 < ${PATCH_DIR}/0001-defconfig-add-overrides-to-resolve-build-error.patch
         patch -f -p1 < ${PATCH_DIR}/0001-redhat-HACK-remove-rpm-build-dependency.patch
    }

    do_patch_config_call || bbwarn "do_patch_config_call failed"

}
addtask patch_config after do_fetch before do_rh_config

do_patch_more() {
    cd ${MY_WDIR}
    patch -f -p1 < ${WORKDIR}/0001-centos-5.14-Fix-to-bypass-redhad-env.patch
    patch -f -p1 < ${WORKDIR}/0002-centos-5.14-build-fixes-while-porting-from-5.4.patch
}
addtask patch_more after do_unpack before do_kernel_metadata
