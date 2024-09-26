SUMMARY = "Platform kernel drivers"
DESCRIPTION = "Build platform drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/dsp-kernel/.git;protocol=${PROTO};name=dspkernel;destsuffix=vendor/qcom/opensource/dsp-kernel;usehead=1 \
    ${PATH_TO_REPO}/vendor/qcom/opensource/platform-kernel/.git;protocol=${PROTO};name=platformkernel;destsuffix=vendor/qcom/opensource/platform-kernel;;usehead=1 \
"
#SRCREV_dspkernel = "${AUTOREV}"
#SRCREV_platformkernel = "${AUTOREV}"
#SRCREV_FORMAT = "dspkernel_platformkernel"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/platform-kernel"

METAL_MODULES_BUILD = "drivers/aop-set-ddr.ko drivers/silent_boot.ko drivers/wallpower_charger.ko drivers/dump_boot_log.ko drivers/silent-mode-hw-monitoring.ko"

VIRT_MODULES_BUILD = "\
    drivers/socinfo_dt.ko \
    drivers/subsystem_notif_virt.ko \
    drivers/virtual_fastrpc/vfastrpc.ko \
"

TECHPACK_MODULES = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '${VIRT_MODULES_BUILD}', '${METAL_MODULES_BUILD}', d)}"

inherit qti-techpack

METAL_PROVIDES_MODULES = "\
    kernel-module-aop-set-ddr-${KERNEL_VERSION} \
    kernel-module-silent-boot-${KERNEL_VERSION} \
    kernel-module-wallpower-charger-${KERNEL_VERSION} \
    kernel-module-dump-boot-log-${KERNEL_VERSION} \
    kernel-module-silent-mode-hw-monitoring-${KERNEL_VERSION} \
"

VIRT_PROVIDES_MODULES = "\
    kernel-module-socinfo-dt-${KERNEL_VERSION} \
    kernel-module-subsystem-notif-virt-${KERNEL_VERSION} \
"
VIRT_PROVIDES_MODULES:append = " kernel-module-vfastrpc-${KERNEL_VERSION}"

do_configure:prepend() {
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_compat.h ${S}/drivers/virtual_fastrpc/dsp/adsprpc_compat.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_shared.h ${S}/drivers/virtual_fastrpc/dsp/adsprpc_shared.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/fastrpc_trace.h ${S}/drivers/virtual_fastrpc/dsp/fastrpc_trace.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/include/uapi/fastrpc_shared.h ${S}/drivers/virtual_fastrpc/dsp/fastrpc_shared.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_compat.c ${S}/drivers/virtual_fastrpc/dsp/adsprpc_compat.c
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_rpmsg.c ${S}/drivers/virtual_fastrpc/dsp/adsprpc_rpmsg.c
}

RPROVIDES:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '${VIRT_PROVIDES_MODULES}', '${METAL_PROVIDES_MODULES}', d)}"

KERNEL_CC:append = " -Wno-error=format"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
