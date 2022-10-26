SUMMARY = "Clang based toolchain to compile QTI kernel"
DESCRIPTION = "LLVM based C/C++ compiler from Android"
HOMEPAGE = "https://android.googlesource.com/toolchain/llvm-project"
LICENSE = "Apache-2.0 & Apache-2.0-with-LLVM-exception & BSD & MIT"
LIC_FILES_CHKSUM = "file://${BASE_PATH}/prebuilts/clang/host/linux-x86/clang-${CLANG_VERSION}/NOTICE;md5=eeec5cfa0edfb54bfdba757236c7b531"

BASE_GIT_PATH = "${PATH_TO_REPO}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform"
BASE_PATH = "kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform"

SRC_URI = " \
    ${BASE_GIT_PATH}/prebuilts/clang/host/linux-x86/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/prebuilts/clang/host/linux-x86 \
    ${BASE_GIT_PATH}/prebuilts/build-tools/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/prebuilts/build-tools \
    ${BASE_GIT_PATH}/prebuilts/kernel-build-tools/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/prebuilts/kernel-build-tools \
    ${BASE_GIT_PATH}/prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.17-4.8/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.17-4.8 \
    ${BASE_GIT_PATH}/prebuilts/ndk-r23/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/prebuilts/ndk-r23 \
    ${BASE_GIT_PATH}/build/kernel/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/build/kernel \
"

SRCREV = "${AUTOREV}"

CLANG_VERSION = "r450784e"

S = "${WORKDIR}"

inherit native qti-kernel-toolchain

INHIBIT_SYSROOT_STRIP = "1"
do_compile[noexec] = "1"
do_configure[noexec] = "1"
do_install[noexec] = "1"

do_shared_workdir () {
    install -d ${KERNEL_TOOLCHAIN_DIR}/build/
    cp -rf ${S}/${BASE_PATH}/build/kernel ${KERNEL_TOOLCHAIN_DIR}/build/
    ln -sf kernel/build.sh ${KERNEL_TOOLCHAIN_DIR}/build/build.sh
    ln -sf kernel/build_module.sh ${KERNEL_TOOLCHAIN_DIR}/build/build_module.sh
    ln -sf kernel/envsetup.sh ${KERNEL_TOOLCHAIN_DIR}/build/envsetup.sh
    ln -sf kernel/_setup_env.sh ${KERNEL_TOOLCHAIN_DIR}/build/_setup_env.sh

    install -d ${KERNEL_TOOLCHAIN_DIR}/prebuilts/clang/host/linux-x86
    cp -rf ${S}/${BASE_PATH}/prebuilts/clang/host/linux-x86/clang-${CLANG_VERSION} ${KERNEL_TOOLCHAIN_DIR}/prebuilts/clang/host/linux-x86/

    cp -rf ${S}/${BASE_PATH}/prebuilts/build-tools ${KERNEL_TOOLCHAIN_DIR}/prebuilts/
    cp -rf ${S}/${BASE_PATH}/prebuilts/kernel-build-tools ${KERNEL_TOOLCHAIN_DIR}/prebuilts/

    install -d ${KERNEL_TOOLCHAIN_DIR}/prebuilts/gcc/linux-x86/host
    cp -rf ${S}/${BASE_PATH}/prebuilts/gcc/linux-x86/host/x86_64-linux-glibc2.17-4.8 ${KERNEL_TOOLCHAIN_DIR}/prebuilts/gcc/linux-x86/host

    install -d ${KERNEL_TOOLCHAIN_DIR}/prebuilts/ndk-r23/toolchains/llvm/prebuilt/linux-x86_64/
    cp -rf ${S}/${BASE_PATH}/prebuilts/ndk-r23/toolchains/llvm/prebuilt/linux-x86_64/sysroot ${KERNEL_TOOLCHAIN_DIR}/prebuilts/ndk-r23/toolchains/llvm/prebuilt/linux-x86_64

    ln -sf prebuilts/clang/host/linux-x86/clang-${CLANG_VERSION} ${KERNEL_TOOLCHAIN_DIR}/clang
}

addtask do_shared_workdir after do_install before do_populate_sysroot
