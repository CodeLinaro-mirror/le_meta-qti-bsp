SUMMARY = "Interface between VIRTIO device implementation and Linux device reset controls"
DESCRIPTION = "This kernel module provides an interface to manipulate Linux kernel reset controls from userspace."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

inherit linux-kernel-base deploy

addtask do_deploy after do_install

do_configure[depends] += "virtual/kernel:do_shared_workdir"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux/ \
                file://BUILD.bazel \
                file://define_modules.bzl"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_compile[lockfiles] = "${TMPDIR}/build_modules.lock"

do_configure() {
    cp ${WORKDIR}/BUILD.bazel ${WORKSPACE}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux/BUILD.bazel
    cp ${WORKDIR}/define_modules.bzl ${WORKSPACE}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux/define_modules.bzl
}

do_compile() {
  cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  &&

  BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
  EXT_MODULES=../../../src/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux \
  ENABLE_DDK_BUILD=${DDK_BUILD} \
  TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
  VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
  ROOTDIR=${WORKSPACE}/ \
  MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux \
  OUT_DIR=temp_out_dir \
  KERNEL_KIT=${KERNEL_OUT_PATH}/ \
  KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
  ./build/build_module.sh
}

do_install() {
    install -d ${D}${includedir}/linux
    install -m 0644 ${S}/include/uapi/linux/urst_ioctl.h ${D}${includedir}/linux/urst_ioctl.h

    install -d ${D}${libdir}/modules-load.d/
    install -m 0644 ${S}/coqos-urst.conf -D ${D}${libdir}/modules-load.d/coqos-urst.conf

    install -d ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${S}/coqos-urst.rules ${D}${sysconfdir}/udev/rules.d/coqos-urst.rules
 
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux/coqos-urst.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/coqos-urst.ko
    chown 0:0 ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/coqos-urst.ko
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-urst-driver-linux/coqos-urst.ko \
        ${DEPLOYDIR}/coqos-urst.ko
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/coqos-urst.ko"
FILES:${PN} += "${libdir}/modules-load.d/coqos-urst.conf"
FILES:${PN} += "${sysconfdir}/udev/rules.d/coqos-urst.rules"
FILES:${PN}-dev += "${includedir}/linux/urst_ioctl.h"

RPROVIDES:${PN} += "kernel-module-qcvirtio-urst-driver-linux"

RM_WORK_EXCLUDE += "${PN}"
