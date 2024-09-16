SUMMARY = "QCOM Linux Kernel"
DESCRIPTION = "QCOM Linux Kernel for QTI SoC"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

COMPATIBLE_MACHINE = "sa8775|sa8797|sa7255"

DEPENDS += "\
    elfutils-native kern-tools-native openssl-native \
    pahole-native rsync-native \
"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/kernel_platform/kernel/.git;protocol=${PROTO};destsuffix=kernel/kernel_platform/kernel;usehead=1 \
    file://generic.cfg \
    file://dm.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'file://selinux.cfg', '', d)} \
    ${@bb.utils.contains_any('VARIANT', 'perf user', '', 'file://devmem.cfg', d)} \
    file://0001-QCLINUX-vfio-Disable-iommu_group_claim_dma_owner-tem.patch \
    file://0002-PENDING-soc-qcom-geni-se-Enable-QUPs-on-SA8255p-Qual.patch \
    file://0003-PENDING-serial-qcom-geni-Enable-Serial-on-SA8255p-pl.patch \
    file://0004-PENDING-i2c-qcom-geni-Enable-I2C-on-SA8255p-Qualcomm.patch \
    file://0005-PENDING-spi-geni-qcom-Enable-SPI-on-SA8255p-Qualcomm.patch \
    file://0006-PENDING-spi-geni-qcom-Enable-SPI-GSI-mode-for-SA8255.patch \
    file://0007-PENDING-scsi-ufs-qcom-Enable-sa8255p-platform.patch \
    file://0008-PENDING-phy-qcom-qmp-usb-Call-qmp_usb_remove-during-.patch \
    file://0009-PENDING-phy-qcom-qmp-usb-Add-support-for-SA8255P.patch \
    file://0010-PENDING-usb-dwc3-qcom-Add-support-for-sa8255p-for-qc.patch \
    file://0011-PENDING-phy-qcom-snps-femto-v2-Call-qcom_snps_hsphy_.patch \
    file://0012-PENDING-phy-qcom-snps-femto-v2-Add-support-for-SA825.patch \
    file://0001-FROMLIST-of-of_reserved_mem-Increase-limit-for-reser.patch \
    file://0013-net-stmmac-dwmac-qcom-ethqos-Enable-SCMI-ETH.patch \
    file://0014-PENDING-qcom-Add-sa7255p-compatibles-for-core-driver.patch \
    file://scm_adci/0001-QCLINUX-arm64-dts-qcom-sa8255p-Modify-correct-dt-nam.patch \
    file://scm_adci/0002-BACKPORT-FROMLIST-firmware-qcom-scm-Support-multiple.patch \
    file://scm_adci/0003-PENDING-firmware-qcom-scm-Add-support-for-WAITQ_WAKE.patch \
    file://scm_adci/0004-PENDING-firmware-qcom-scm-Selectively-skip-mutex-for.patch \
    file://scm_adci/0005-UPSTREAM-firmware-qcom-scm-Remove-QCOM_SMC_WAITQ_FLA.patch \
    file://scm_adci/0006-PENDING-firmware-qcom-scm-Introduce-new-locking-mech.patch \
    file://scm_adci/0007-BACKPORT-UPSTREAM-firmware-qcom-scm-Mark-get_wq_ctx-.patch \
    file://scm_adci/0008-BACKPORT-UPSTREAM-firmware-qcom-scm-add-support-for-.patch \
"

SRCREV_kernel = "${AUTOREV}"

inherit kernel kernel-yocto

S = "${WORKDIR}/kernel/kernel_platform/kernel"

KERNEL_ARCH ?= "gen4auto"
KBRANCH ?= ""
KMETA = "kernel-meta"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG ?= "${KERNEL_CONFIG}"
LINUX_VERSION_EXTENSION = "${@['-perf', ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"

do_kernel_checkout[noexec] = "1"
do_validate_branches[noexec] = "1"

do_generate_base_defconfig() {
    export KCONFIG_CONFIG="${S}/arch/arm64/configs/${KBUILD_DEFCONFIG}"
    base_defconfig="${S}/arch/arm64/configs/qcom_defconfig"
    kernel_arch_config="${S}/arch/arm64/configs/qcom_${KERNEL_ARCH}.config"
    kernel_arch_config+="${@bb.utils.contains_any('VARIANT', 'debug user', ' ${S}/arch/arm64/configs/qcom_${KERNEL_ARCH}_debug.config', '', d)}"
    ${S}/scripts/kconfig/merge_config.sh -m -r -y ${base_defconfig} ${kernel_arch_config} 1>&2
}
addtask do_generate_base_defconfig after do_unpack before do_kernel_metadata

do_patch:append() {
    cd ${S}
    patch -f -p1 < ${WORKDIR}/0001-QCLINUX-vfio-Disable-iommu_group_claim_dma_owner-tem.patch
    patch -f -p1 < ${WORKDIR}/0002-PENDING-soc-qcom-geni-se-Enable-QUPs-on-SA8255p-Qual.patch
    patch -f -p1 < ${WORKDIR}/0003-PENDING-serial-qcom-geni-Enable-Serial-on-SA8255p-pl.patch
    patch -f -p1 < ${WORKDIR}/0004-PENDING-i2c-qcom-geni-Enable-I2C-on-SA8255p-Qualcomm.patch
    patch -f -p1 < ${WORKDIR}/0005-PENDING-spi-geni-qcom-Enable-SPI-on-SA8255p-Qualcomm.patch
    patch -f -p1 < ${WORKDIR}/0006-PENDING-spi-geni-qcom-Enable-SPI-GSI-mode-for-SA8255.patch
    patch -f -p1 < ${WORKDIR}/0007-PENDING-scsi-ufs-qcom-Enable-sa8255p-platform.patch
    patch -f -p1 < ${WORKDIR}/0008-PENDING-phy-qcom-qmp-usb-Call-qmp_usb_remove-during-.patch
    patch -f -p1 < ${WORKDIR}/0009-PENDING-phy-qcom-qmp-usb-Add-support-for-SA8255P.patch
    patch -f -p1 < ${WORKDIR}/0010-PENDING-usb-dwc3-qcom-Add-support-for-sa8255p-for-qc.patch
    patch -f -p1 < ${WORKDIR}/0011-PENDING-phy-qcom-snps-femto-v2-Call-qcom_snps_hsphy_.patch
    patch -f -p1 < ${WORKDIR}/0012-PENDING-phy-qcom-snps-femto-v2-Add-support-for-SA825.patch
    patch -f -p1 < ${WORKDIR}/0001-FROMLIST-of-of_reserved_mem-Increase-limit-for-reser.patch
    patch -f -p1 < ${WORKDIR}/0013-net-stmmac-dwmac-qcom-ethqos-Enable-SCMI-ETH.patch
    patch -f -p1 < ${WORKDIR}/0014-PENDING-qcom-Add-sa7255p-compatibles-for-core-driver.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0001-QCLINUX-arm64-dts-qcom-sa8255p-Modify-correct-dt-nam.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0002-BACKPORT-FROMLIST-firmware-qcom-scm-Support-multiple.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0003-PENDING-firmware-qcom-scm-Add-support-for-WAITQ_WAKE.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0004-PENDING-firmware-qcom-scm-Selectively-skip-mutex-for.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0005-UPSTREAM-firmware-qcom-scm-Remove-QCOM_SMC_WAITQ_FLA.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0006-PENDING-firmware-qcom-scm-Introduce-new-locking-mech.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0007-BACKPORT-UPSTREAM-firmware-qcom-scm-Mark-get_wq_ctx-.patch
    patch -f -p1 < ${WORKDIR}/scm_adci/0008-BACKPORT-UPSTREAM-firmware-qcom-scm-add-support-for-.patch
}

do_compile:prepend() {
    export DTC_FLAGS="-@"
}

do_shared_workdir:append () {
    mkdir -p $kerneldir/certs
    install -m 0644 certs/signing_key.x509 $kerneldir/certs/
    rsync -av --exclude=*.cmd --exclude=*.o scripts $kerneldir

    install -m 0644 Makefile $kerneldir/
    cp -fR usr $kerneldir/

    install -m 0644 include/config/auto.conf $kerneldir/include/config/auto.conf

    if [ -d arch/${ARCH}/include ]; then
        mkdir -p $kerneldir/arch/${ARCH}/include/
        cp -fR arch/${ARCH}/include/* $kerneldir/arch/${ARCH}/include/
    fi

    # Generate kernel headers
    oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}

    if (grep -q -i -e '^CONFIG_MODULES=y$' ${B}/.config); then
        # Module.symvers gets updated during the
        # building of the kernel modules. We need to
        # update this in the shared workdir since some
        # external kernel modules has a dependency on
        # other kernel modules and will look at this
        # file to do symbol lookups
        cp ${B}/Module.symvers ${STAGING_KERNEL_BUILDDIR}/
        # 5.10+ kernels have module.lds that we need to copy for external module builds
        if [ -e "${B}/scripts/module.lds" ]; then
            install -Dm 0644 ${B}/scripts/module.lds ${STAGING_KERNEL_BUILDDIR}/scripts/module.lds
        fi
    fi
}

# Kernel 6.6 with commit d8131c2965d5ee59bfa4d548641e52a13cbe17c9
# removed $(MODLIB)/source symlink. However, kernel.bbclass is yet to
# catchup and failing while removing a non-existing link. To mitigate,
# added a dummy link which would be deleted by kernel_do_install task.
do_install:prepend() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    ln -rs ${STAGING_KERNEL_DIR} ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/source
}

KERNEL_MACHINE_DTB ?= ""

do_deploy:append() {
    # Copy Image and dtbs to deploydir
    install -m 0644 vmlinux ${DEPLOYDIR}

    if [ -n "${KERNEL_MACHINE_DTB}" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/dtb

        for dtb in ${KERNEL_MACHINE_DTB}; do
            if [ -f ${B}/$dtb ]; then
                install -m 0644 ${B}/$dtb ${DEPLOYDIR}/build-artifacts/dtb
            fi
        done
    fi

    if [ -n "${KERNEL_BASE_DTB}" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/kernel-dtb

        for dtb in ${KERNEL_BASE_DTB}; do
            if [ -f ${B}/$dtb ]; then
                install -m 0644 ${B}/$dtb ${DEPLOYDIR}/build-artifacts/kernel-dtb
            fi
        done
    fi
}

INHIBIT_PACKAGE_STRIP = "1"
KERNEL_VERSION_SANITY_SKIP = "1"
KERNEL_IMAGETYPE_FOR_MAKE += "dtbs"
KERNEL_IMAGETYPE_FOR_MAKE += "${KERNEL_IMAGETYPE}"
KERNEL_IMAGETYPE_FOR_MAKE += "modules"

do_compile_kernelmodules[noexec] = "1"

