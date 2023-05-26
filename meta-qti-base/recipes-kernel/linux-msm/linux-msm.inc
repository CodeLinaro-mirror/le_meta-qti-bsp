DEPENDS += "elfutils-native kern-tools-native mkbootimg-native mkdtimg-native openssl-native pahole-native rsync-native signing-keys"

COMPATIBLE_MACHINE = "sa81x5|lemans|quin-gvm-gen4-2|quin-gvm-lemans|monaco|qtiquingvm8295"

SRC_URI = "${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/msm-kernel/.git;protocol=${PROTO};name=kernel;destsuffix=kernel/kernel-${PV}/kernel_platform/msm-kernel;usehead=1"

SRCREV_kernel = "${AUTOREV}"

inherit kernel kernel-yocto qti-kernel-arch-clang

# Due to inherit kernel. If choose clang as a compilation chain, need unset thist variable to set clang as BASEDEPENDS.
unset INHIBIT_DEFAULT_DEPS

LDFLAGS:aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"

KERNEL_CONFIG_PATH = "${S}/arch/${ARCH}/configs"

#dts path is changed to vendor/qcom
DTB_SRC_PATH = "${STAGING_KERNEL_BUILDDIR}/arch/${ARCH}/boot/dts/vendor/qcom"
KERNEL_CONFIG_COMMAND ?= "oe_runmake_call -C ${S} CC="${KERNEL_CC}" LD="${KERNEL_LD}" O=${B} || oe_runmake -C ${S} O=${B} CC="${KERNEL_CC}" LD="${KERNEL_LD}""
get_cc_option () {
:
}

DEPENDS:append:aarch64 = " libgcc"
KERNEL_CC:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_PRIORITY = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS += "O=${B}"

LINUX_VERSION_EXTENSION = "${@bb.utils.contains_any('VARIANT', 'perf user', '-perf', '-debug', d)}"
LINUX_VERSION_EXTENSION_qti-distro-debug = ""

# dm-verity: Patch the cert file from which kernel add key to keyring
do_patch_veritycert() {
   cp -f ${WORKDIR}/verity.x509.pem ${S}/certs/verity.x509.pem
}

do_patch[postfuncs] += "${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', bb.utils.contains('MACHINE_FEATURES', 'dm-verity-bootloader', 'do_patch_veritycert', '', d), '', d)}"

EXTRA_OEMAKE:remove = "PAHOLE=false"
KCONFIG_CONFIG_COMMAND:remove = "PAHOLE=false"

KMETA = "kernel-meta"
KMACHINE ?= "${KERNEL_ARCH}"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG ?= "vendor/${KERNEL_ARCH}${@bb.utils.contains_any('VARIANT', 'debug user', '-debug', '', d)}_defconfig"

KERNEL_VERSION_SANITY_SKIP = "1"

# append DTB
# msm kernel trees have a special treatment for DTS, and both arm and
# arm64 DTS are located in arch/arm64/boot/dts/qcom folder, which
# confuses kernel-devicetree class, so we can't use it. Instead let's
# make sure that we generate all DTBs using the kernel 'dtbs' target,
# then we can append the DTBs that we need for $MACHINE.
KERNEL_IMAGETYPE_FOR_MAKE += "dtbs"
KERNEL_IMAGETYPE_FOR_MAKE += "${KERNEL_IMAGETYPE}"
KERNEL_IMAGETYPE_FOR_MAKE += "modules"

do_compile_kernelmodules[noexec] = "1"

do_kernel_configcheck[noexec] = "1"
do_kernel_checkout[noexec] = "1"
do_validate_branches[noexec] = "1"

do_generate_base_defconfig() {
    export KCONFIG_CONFIG=${KERNEL_CONFIG_PATH}/${KBUILD_DEFCONFIG}
    if [ ! -f "${KERNEL_CONFIG_PATH}/vendor/${KERNEL_ARCH}.config" ]; then
        bbfatal "KERNEL_CONFIG '${KERNEL_ARCH}.config' was specified, but not present in the source tree"
    fi

    base_defconfig="${KERNEL_CONFIG_PATH}/generic_auto_defconfig"
    kernel_defconfigs="${KERNEL_CONFIG_PATH}/vendor/${KERNEL_ARCH}.config ${@bb.utils.contains_any('VARIANT', 'debug user', '${KERNEL_CONFIG_PATH}/vendor/${KERNEL_ARCH}_debug.config', '', d)}"
    ${S}/scripts/kconfig/merge_config.sh -m -r -y ${base_defconfig} ${kernel_defconfigs} 1>&2
}
addtask do_generate_base_defconfig after do_unpack before do_kernel_metadata
do_generate_base_defconfig[depends] += "virtual/${TARGET_PREFIX}binutils:do_populate_sysroot"
do_generate_base_defconfig[depends] += "clang-cross-${TARGET_ARCH}:do_populate_sysroot"

do_configure:prepend() {
    echo "# Global settings from linux recipe" >> ${B}/.config
    echo "CONFIG_LOCALVERSION="\"${LINUX_VERSION_EXTENSION}\" >> ${B}/.config
}

MODULESPATH = "${D}/lib/modules/${KERNEL_VERSION}"
do_install:append() {
    # if the blocklist exists and qti-dlkm is turned on in conf file, then install the blocklist
    if [ -e ${S}/modules.vendor_blocklist.msm.${KERNEL_ARCH} ] && ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dlkm', 'true', 'false', d)}; then
        install -m 0644 ${S}/modules.vendor_blocklist.msm.${KERNEL_ARCH} ${MODULESPATH}/modules.blocklist
    fi
}

do_compile:prepend() {
        if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
            export DTC_FLAGS="-@"
        fi
}

# when using our own module signing key kernel.bbclass will fail to copy the public part of the key
# since it checks if the .pem file exists which is not the case, so we need to explicitely copy
# the x509 (public key) file
do_shared_workdir:append () {
        mkdir -p $kerneldir/certs
        cp certs/signing_key.x509 $kerneldir/certs/
        rsync -av --exclude=*.cmd --exclude=*.o scripts $kerneldir

        cp Makefile $kerneldir/
        cp -fR usr $kerneldir/

        cp include/config/auto.conf $kerneldir/include/config/auto.conf

        if [ -d arch/${ARCH}/include ]; then
                mkdir -p $kerneldir/arch/${ARCH}/include/
                cp -fR arch/${ARCH}/include/* $kerneldir/arch/${ARCH}/include/
        fi

        if [ -d arch/${ARCH}/boot/dts/vendor ]; then
                mkdir -p $kerneldir/arch/${ARCH}/boot/dts/vendor
                cp -fR arch/${ARCH}/boot/dts/vendor/* $kerneldir/arch/${ARCH}/boot/dts/vendor
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

# Path for dtbo generation is kernel version dependent.
DTB_SRC_PATH ?= "${STAGING_KERNEL_BUILDDIR}/arch/${ARCH}/boot/dts/qcom"

do_deploy() {
    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi

    # Copy vmlinux and zImage into deplydir for boot.img creation
    install -d ${DEPLOYDIR}
    install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}
    install -m 0644 vmlinux ${DEPLOYDIR}
    install -m 0644 System.map ${DEPLOYDIR}

    # copy initramfs scripts
     install -d ${DEPLOYDIR}/build-artifacts
     install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
     install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr/
     install -d ${DEPLOYDIR}/build-artifacts/dtb/
     install -d ${DEPLOYDIR}/build-artifacts/dtbo/

     cp  ${S}/usr/gen_initramfs.sh ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
     cp -a ${B}/usr/gen_init_cpio ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr/
     cp -a ${B}/usr/initramfs_data.cpio ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr/
     cp -a ${B}/usr/initramfs_inc_data ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr/
     cp -a ${DTB_SRC_PATH}/*.dtb ${DEPLOYDIR}/build-artifacts/dtb/
     cp -a ${DTB_SRC_PATH}/*.dtbo ${DEPLOYDIR}/build-artifacts/dtbo/
}

# Put the zImage in the kernel-dev pkg
FILES:${KERNEL_PACKAGE_NAME}-dev += "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}"

FILES:${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.blocklist \
                                      ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.load"
