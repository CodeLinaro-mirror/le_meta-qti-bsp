DESCRIPTION = "Clang based toolchain to compile QTI kernel"
LICENSE = "Apache-2.0 & MIT & NCSA"
LIC_FILES_CHKSUM = "file://NOTICE;md5=eeec5cfa0edfb54bfdba757236c7b531"

PROVIDES = "virtual/kernel-toolchain-native"

FILESEXTRAPATHS:prepend := "${KERNEL_PLATFORM_PATH}/prebuilts/clang/host/linux-x86/:"
SRC_URI    = "file://clang-${CLANG_VERSION}"
CLANG_VERSION = "r510928"

S = "${WORKDIR}/clang-${CLANG_VERSION}"
INHIBIT_SYSROOT_STRIP = "1"
do_compile[noexec] = "1"
do_configure[noexec] = "1"

BBCLASSEXTEND = " native"

do_install() {
    install -d ${D}/${bindir}/clang/
    install -d ${D}/${bindir}/clang/bin/
    cp -rf ${S}/bin/* ${D}/${bindir}/clang/bin/
}
