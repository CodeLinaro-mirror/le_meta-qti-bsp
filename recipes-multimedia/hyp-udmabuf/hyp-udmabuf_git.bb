SUMMARY = "Hyp udmabuf Kernel Modules"
DESCRIPTION = "This is the hyp udmabuf driver used to share dmabufs cross VMs."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

inherit linux-kernel-base deploy

addtask do_deploy after do_install

do_configure[depends] += "virtual/kernel:do_shared_workdir"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

FILESPATH   =+ "${WORKSPACEROOT}:"
SRC_URI     =  "file://vendor/qcom/opensource/hyp-udmabuf/drivers/"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/drivers"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_compile[lockfiles] = "${TMPDIR}/build_modules.lock"

do_configure() {
    :
}

do_compile() {
  cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  &&

  BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
  EXT_MODULES=../../../vendor/qcom/opensource/hyp-udmabuf/drivers \
  ENABLE_DDK_BUILD=${DDK_BUILD} \
  TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
  VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
  ROOTDIR=${WORKSPACE}/ \
  MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/drivers \
  OUT_DIR=temp_out_dir \
  KERNEL_KIT=${KERNEL_OUT_PATH}/ \
  KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
  ./build/build_module.sh
}

do_install() {
    install -d ${D}${includedir}/linux
    install -d ${D}${libdir}/modules-load.d/
    install -m 0644 ${S}/include/uapi/linux/hyp_udmabuf.h ${D}${includedir}/linux
    install -m 0755 ${S}/hyp-udmabuf.conf -D ${D}${libdir}/modules-load.d/hyp-udmabuf.conf

    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/drivers/hyp-udmabuf.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    chown 0:0 ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/hyp-udmabuf.ko
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/drivers/hyp-udmabuf.ko \
        ${DEPLOYDIR}/
}
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${libdir}/modules-load.d/*"

RPROVIDES:${PN} += "kernel-module-hyp-udmabuf-${KERNEL_VERSION}"

RM_WORK_EXCLUDE += "${PN}"