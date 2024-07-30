SUMMARY = "QCOM Linux Kernel"
DESCRIPTION = "QCOM Linux Kernel for QTI SoC"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

COMPATIBLE_MACHINE = "sa8775"

DEPENDS += "\
    elfutils-native kern-tools-native openssl-native \
    pahole-native rsync-native \
"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/kernel_platform/kernel/.git;protocol=${PROTO};destsuffix=kernel/kernel_platform/kernel;usehead=1 \
    file://generic.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'file://selinux.cfg', '', d)} \
    ${@bb.utils.contains_any('VARIANT', 'perf user', '', 'file://devmem.cfg', d)} \
"

SRCREV_kernel = "${AUTOREV}"

inherit kernel kernel-yocto

S = "${WORKDIR}/kernel/kernel_platform/kernel"

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
    qcom_addon_config="${S}/arch/arm64/configs/qcom_addons.config"
    ${S}/scripts/kconfig/merge_config.sh -m -r -y ${base_defconfig} ${qcom_addon_config} 1>&2
}
addtask do_generate_base_defconfig after do_unpack before do_kernel_metadata

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

