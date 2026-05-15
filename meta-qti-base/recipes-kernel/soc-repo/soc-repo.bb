SUMMARY = "Qualcomm SoC kernel modules"
DESCRIPTION = "Out-of-tree kernel modules for Qualcomm SoC platforms"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

COMPATIBLE_MACHINE = "gvm-gen5"

PV = "1.0"

# Inherit kernel module class
inherit qti-techpack

# Disable automatic make clean in base_do_configure
CLEANBROKEN = "1"

# Source location
SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/platform-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/platform-kernel;usehead=1 \
    file://0001-soc-repo-adapt-cse-linux-6.6.patch \
    file://0002-Compatibility-modifications-for-soc-repo-SMMUv2.patch \
    file://0003-Compatibility-modifications-for-gh-and-wdt-driver.patch "

SRCREV = "${AUTOREV}"

# Kernel source dependency - must build after linux-qcom-rt
DEPENDS = "virtual/kernel linux-qcom-rt"

# Ensure we build after linux-qcom-rt shared workdir is ready
do_compile[depends] += "linux-qcom-rt:do_shared_workdir"

# Source directory
S = "${WORKDIR}/vendor/qcom/opensource/platform-kernel/qclinux/soc-repo"
SOC_REPO_MAKE = "${WORKDIR}/vendor/qcom/opensource/platform-kernel/qclinux/soc-repo-make"
DEFCONFIG_FILE = "${WORKDIR}/vendor/qcom/opensource/platform-kernel/qclinux/defconfig/autogvmlv_defconfig"

# Configure: Overlay makefiles and headers
do_configure:append() {
    # Remove all Kbuild files to ensure only our Makefiles are used
    # Kbuild files have higher priority than Makefiles in kernel build system
    find ${S} -name "Kbuild" -type f -delete
    find ${S} -name "Makefile" -type f -delete

    # Copy makefiles to control module selection
    if [ -d ${SOC_REPO_MAKE}/makefiles ]; then
        cp -rf ${SOC_REPO_MAKE}/makefiles/* ${S}/
    else
        bbfatal "makefiles directory not found in soc-repo-make"
    fi

    # Overlay soc-repo headers to kernel source to ensure they take precedence
    if [ -d ${S}/include ]; then
        cp -rf ${S}/include/* ${STAGING_KERNEL_DIR}/include/
    fi

    if [ -d ${S}/arch/arm64/include ]; then
        cp -rf ${S}/arch/arm64/include/* ${STAGING_KERNEL_DIR}/arch/arm64/include/
    fi
}

# Compile: Build modules using standard OOT approach
do_compile() {
    DEFCONFIG_CFLAGS=""
    CONFIG_VARS=""

    if [ -f ${DEFCONFIG_FILE} ]; then
        DEFCONFIG_CFLAGS=$(grep -E '^CONFIG_.*=[ym]$' ${DEFCONFIG_FILE} | \
                          sed 's/=.*//' | \
                          sed 's/^/-D/' | \
                          tr '\n' ' ')

        # Append int/hex type CONFIG values as -DCONFIG_FOO=value
        INT_CFLAGS=$(grep -E '^CONFIG_.*=[0-9]+$' ${DEFCONFIG_FILE} | \
                     sed 's/^\(CONFIG_[^=]*\)=\(.*\)$/-D\1=\2/' | \
                     tr '\n' ' ')
        DEFCONFIG_CFLAGS="${DEFCONFIG_CFLAGS} ${INT_CFLAGS}"

        CONFIG_VARS=$(grep -E '^CONFIG_.*=[ym]$' ${DEFCONFIG_FILE} | \
                     sed 's/=y/=m/' | \
                     tr '\n' ' ')
    else
        bbwarn "autogvmlv_defconfig not found at ${DEFCONFIG_FILE}"
    fi

    # Standard out-of-tree module build with defconfig flags
    # soc-repo headers have been overlaid to kernel source in do_configure
    # CONFIG_VARS: Pass CONFIG options as make variables for Makefile conditionals
    # EXTRA_CFLAGS: Add kernel source dir and CONFIG definitions for C compilation
    # V=1 shows full compiler command for debugging
    oe_runmake -C ${STAGING_KERNEL_BUILDDIR} \
        M=${S} \
        ARCH=${ARCH} \
        CROSS_COMPILE=${TARGET_PREFIX} \
        ${CONFIG_VARS} \
        EXTRA_CFLAGS="-I${STAGING_KERNEL_DIR} ${DEFCONFIG_CFLAGS}" \
        KCFLAGS="-Wno-error=implicit-fallthrough -Wno-error=unused-variable -Wno-error=format -Wno-error=incompatible-pointer-types" \
        modules
}

TECHPACK_MODULES = "\
    drivers/virt/gunyah/gh_dbl.ko \
    drivers/virt/gunyah/gh_msgq.ko \
    drivers/virt/gunyah/gh_rm_drv.ko \
    arch/arm64/gunyah/gh_arm_drv.ko \
    drivers/soc/qcom/hab/msm_hab.ko \
    drivers/virtio/virtio_mmio.ko \
    drivers/iommu/arm/arm-smmu/arm_smmu.ko \
    drivers/iommu/iommu-logger.ko \
    drivers/iommu/qcom_iommu_util.ko \
    drivers/soc/qcom/qcom_wdt_core.ko \
    drivers/soc/qcom/qcom_soc_wdt.ko \
    drivers/soc/qcom/minidump.ko \
    drivers/soc/qcom/debug_symbol.ko \
"

# Exclude KERNEL_VERSION from task hash calculation to avoid metadata instability
# KERNEL_VERSION is read dynamically and may cause basehash changes during reparsing
do_patch[vardepsexclude] += "KERNEL_VERSION"
do_populate_sysroot[vardepsexclude] += "KERNEL_VERSION"
do_deploy_source_date_epoch[vardepsexclude] += "KERNEL_VERSION"
