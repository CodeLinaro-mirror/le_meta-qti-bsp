SUMMARY = "Clang based toolchain to compile QTI kernel"
DESCRIPTION = "LLVM based C/C++ compiler from Android"
HOMEPAGE = "https://android.googlesource.com/toolchain/llvm-project"
LICENSE = "Apache-2.0 & Apache-2.0-with-LLVM-exception & BSD & MIT"
LIC_FILES_CHKSUM = "file://${BASE_PATH}/prebuilts/clang/host/linux-x86/clang-${CLANG_VERSION}/NOTICE;md5=eeec5cfa0edfb54bfdba757236c7b531"

BASE_GIT_PATH = "${PATH_TO_REPO}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform"
BASE_PATH = "kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform"

SRC_URI = " \
    ${BASE_GIT_PATH}/prebuilts/clang/host/linux-x86/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/prebuilts/clang/host/linux-x86 \
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
    ln -sf kernel/android ${KERNEL_TOOLCHAIN_DIR}/build/android
}

addtask do_shared_workdir after do_install before do_populate_sysroot
