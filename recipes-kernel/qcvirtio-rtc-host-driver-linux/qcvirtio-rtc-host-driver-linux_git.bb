SUMMARY = "Interface between VIRTIO device implementation and Linux device RTC"
DESCRIPTION = "This kernel module enables high-precision synchronization between the guest and host clocks"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

inherit linux-kernel-base deploy

addtask do_deploy after do_install

do_configure[depends] += "virtual/kernel:do_shared_workdir"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux/ \
                file://BUILD.bazel \
                file://define_modules.bzl \
                file://virtio-rtc-host.conf"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_compile[lockfiles] = "${TMPDIR}/build_modules.lock"

do_configure() {
    cp ${WORKDIR}/BUILD.bazel ${WORKSPACE}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux/BUILD.bazel
    cp ${WORKDIR}/define_modules.bzl ${WORKSPACE}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux/define_modules.bzl
    cp ${WORKDIR}/virtio-rtc-host.conf ${WORKSPACE}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux/virtio-rtc-host.conf
}

do_compile() {
  cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  &&

  BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
  EXT_MODULES=../../../src/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux \
  ENABLE_DDK_BUILD=${DDK_BUILD} \
  TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
  VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
  ROOTDIR=${WORKSPACE}/ \
  MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux \
  OUT_DIR=temp_out_dir \
  KERNEL_KIT=${KERNEL_OUT_PATH}/ \
  KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
  ./build/build_module.sh
}

do_install() {
    install -d ${D}${includedir}/linux
    install -m 0644 ${S}/include/uapi/linux/virtio_rtc_host_ioctl.h ${D}${includedir}/linux/virtio_rtc_host_ioctl.h

    install -d ${D}${libdir}/modules-load.d/
    install -m 0644 ${WORKDIR}/virtio-rtc-host.conf -D ${D}${libdir}/modules-load.d/virtio-rtc-host.conf
 
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux/virtio_rtc_host.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/virtio_rtc_host.ko
    chown 0:0 ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/virtio_rtc_host.ko
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-rtc-host-driver-linux/virtio_rtc_host.ko \
        ${DEPLOYDIR}/virtio_rtc_host.ko
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/virtio_rtc_host.ko"
FILES:${PN} += "${libdir}/modules-load.d/virtio-rtc-host.conf"
FILES:${PN}-dev += "${includedir}/linux/virtio_rtc_host_ioctl.h"
RPROVIDES:${PN} += "kernel-module-qcvirtio-rtc-host-driver-linux"

RM_WORK_EXCLUDE += "${PN}"
