SUMMARY = "CLO Linux Kernel Headers"
DESCRIPTION = "Installs MSM kernel headers required to build userspace. \
These headers are installed in ${includedir}/linux-msm path."
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESPATH =+ "${SRC_DIR_ROOT}/kernel:"
SRC_URI = "file://kernel-${PV}/kernel_platform/msm-kernel"

S = "${WORKDIR}/kernel-${PV}/kernel_platform/msm-kernel"

inherit kernel-arch pkgconfig multilib_header

KERNEL_VARIANT = "${@bb.utils.contains_any('VARIANT', 'perf user', 'perf_', 'debug_', d)}"
KERNEL_PREBUILT_PATH ?= "${SRC_DIR_ROOT}/kernel/kernel-${PV}/out/msm-kernel-${BASEMACHINE}-${KERNEL_VARIANT}defconfig/dist"
KERNEL_PREBUILT_PATH:sa81x5 = "${SRC_DIR_ROOT}/kernel/kernel-${PV}/out/msm-kernel-gen3auto-${KERNEL_VARIANT}defconfig/dist"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_populate_kernel_header_artifacts() {
    mkdir -p ${B}/headers
    cp -a ${KERNEL_PREBUILT_PATH}/kernel-uapi-headers.tar.gz ${B}/headers
    cd ${B}/headers
    tar -xvzf kernel-uapi-headers.tar.gz
    rm -f kernel-uapi-headers.tar.gz
}

addtask do_populate_kernel_header_artifacts after do_compile before do_install

do_install () {
    cd ${B}
    headerdir=${B}/headers
    kerneldir=${D}${includedir}/linux-msm
    install -d $kerneldir

    if [ -d $headerdir/${includedir} ]; then
        mkdir -p $kerneldir/${includedir}
        cp -fR $headerdir/${includedir}/* $kerneldir/${includedir}
    fi
}

# kernel headers are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Allow to build empty main package, to include -dev package into the SDK
ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-dev += "linux-msm/*"

INHIBIT_DEFAULT_DEPS = "1"
