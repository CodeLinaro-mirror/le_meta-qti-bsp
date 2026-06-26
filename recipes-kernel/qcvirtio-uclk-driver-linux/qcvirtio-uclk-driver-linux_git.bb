SUMMARY = "Interface between VIRTIO device implementation and Linux device clocks"
DESCRIPTION = "This kernel module provides an interface to manipulate Linux kernel clocks from userspace."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

inherit linux-kernel-base deploy

addtask do_deploy after do_install

do_configure[depends] += "virtual/kernel:do_shared_workdir"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux/ \
                file://BUILD.bazel \
                file://define_modules.bzl"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_compile[lockfiles] = "${TMPDIR}/build_modules.lock"

do_configure() {
    cp ${WORKDIR}/BUILD.bazel ${WORKSPACE}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux/BUILD.bazel
    cp ${WORKDIR}/define_modules.bzl ${WORKSPACE}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux/define_modules.bzl
}

do_compile() {
  cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  &&

  BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
  EXT_MODULES=../../../src/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux \
  ENABLE_DDK_BUILD=${DDK_BUILD} \
  TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
  VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
  ROOTDIR=${WORKSPACE}/ \
  MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux \
  OUT_DIR=temp_out_dir \
  KERNEL_KIT=${KERNEL_OUT_PATH}/ \
  KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
  ./build/build_module.sh
}

do_install() {
    install -d ${D}${includedir}/linux
    install -m 0644 ${S}/include/uapi/linux/uclk_ioctl.h ${D}${includedir}/linux/uclk_ioctl.h

    install -d ${D}${libdir}/modules-load.d/
    install -m 0644 ${S}/coqos-uclk.conf -D ${D}${libdir}/modules-load.d/coqos-uclk.conf

    install -d ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${S}/coqos-uclk.rules ${D}${sysconfdir}/udev/rules.d/coqos-uclk.rules
 
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux/coqos-uclk.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/coqos-uclk.ko
    chown 0:0 ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/coqos-uclk.ko
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-uclk-driver-linux/coqos-uclk.ko \
        ${DEPLOYDIR}/coqos-uclk.ko
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/coqos-uclk.ko"
FILES:${PN} += "${libdir}/modules-load.d/coqos-uclk.conf"
FILES:${PN} += "${sysconfdir}/udev/rules.d/coqos-uclk.rules"
FILES:${PN}-dev += "${includedir}/linux/uclk_ioctl.h"

RPROVIDES:${PN} += "kernel-module-qcvirtio-uclk-driver-linux"

RM_WORK_EXCLUDE += "${PN}"
