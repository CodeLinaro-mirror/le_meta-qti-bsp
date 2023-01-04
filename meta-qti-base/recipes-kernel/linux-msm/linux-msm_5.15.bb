SUMMARY = "CLO Linux Kernel"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "elfutils-native kern-tools-native kernel-toolchain-native mkbootimg-native mkdtimg-native openssl-native pahole-native rsync-native signing-keys"

COMPATIBLE_MACHINE = "sa81x5|lemans|quin-gvm-gen4-2"

FILESPATH =+ "${KERNEL_SRC_PATH}:"
SRC_URI = "${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/msm-kernel/.git;protocol=${PROTO};destsuffix=kernel/kernel-${PV}/kernel_platform/msm-kernel;usehead=1"

SRCREV = "${AUTOREV}"

inherit kernel qti-kernel-toolchain

S = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/msm-kernel"

LDFLAGS:aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"

KERNEL_CC = "${KERNEL_TOOLCHAIN_CLANG}/bin/clang"
KERNEL_CONFIG_PATH = "${S}/arch/${ARCH}/configs"

KERNEL_PREBUILT_PATH ?= "${SRC_DIR_ROOT}/kernel/kernel-${PV}/out/msm-kernel-${KERNEL_ARCH}-${KERNEL_VARIANT}defconfig/dist"

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

# Don't set any version extention on debug build
LINUX_VERSION_EXTENSION ?= "-perf"
LINUX_VERSION_EXTENSION_qti-distro-debug = ""

# dm-verity: Patch the cert file from which kernel add key to keyring
do_patch_veritycert() {
   cp -f ${WORKDIR}/verity.x509.pem ${S}/certs/verity.x509.pem
}

do_patch[postfuncs] += "${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', bb.utils.contains('MACHINE_FEATURES', 'dm-verity-bootloader', 'do_patch_veritycert', '', d), '', d)}"

EXTRA_OEMAKE:remove = "PAHOLE=false"
KCONFIG_CONFIG_COMMAND:remove = "PAHOLE=false"

do_configure:prepend() {
    if [ ! -f "${KERNEL_CONFIG_PATH}/vendor/${KERNEL_ARCH}.config" ]; then
        bbfatal "KERNEL_CONFIG '${KERNEL_ARCH}.config' was specified, but not present in the source tree"
    fi

    base_defconfig="${KERNEL_CONFIG_PATH}/generic_auto_defconfig"
    kernel_defconfigs="${KERNEL_CONFIG_PATH}/vendor/${KERNEL_ARCH}.config ${@bb.utils.contains_any('VARIANT', 'debug user', '${KERNEL_CONFIG_PATH}/vendor/${KERNEL_ARCH}_debug.config', '', d)}"
    ${S}/scripts/kconfig/merge_config.sh -m -r -y -O ${B} ${base_defconfig} ${kernel_defconfigs} 1>&2

    echo "# Global settings from linux recipe" >> ${B}/.config
    echo "CONFIG_LOCALVERSION="\"${LINUX_VERSION_EXTENSION}\" >> ${B}/.config
}

do_prebuilt_configure() {
    cd ${KERNEL_PREBUILT_PATH}

    install -d ${B}/include/config
    install -d ${B}/include/generated
    install -d ${B}/scripts
    install -d ${B}/certs
    # Some of the artifacts needed for module compilation are present under
    # msm-kernel path, for now copy them for this path to avoid build failures.
    # Ask prebuilt providers to make these available in KERNEL_PREBUILT_PATH.
    install -m 0644 ../msm-kernel/.config ${B}
    install -m 0644 ../msm-kernel/Makefile ${B}
    install -m 0644 ../msm-kernel/Module.symvers ${B}
    install -m 0644 ../msm-kernel/include/config/auto.conf ${B}/include/config/auto.conf
    install -m 0644 ../msm-kernel/include/config/kernel.release ${B}/include/config/kernel.release
    install -m 0644 ../msm-kernel/include/generated/utsrelease.h ${B}/include/generated
    install -m 0644 ../msm-kernel/certs/* ${B}/certs
    cp -R ../msm-kernel/scripts/ ${B}/
    cp -R ../msm-kernel/include/generated/ ${B}/include/

    if [ -d ../msm-kernel/arch/${ARCH}/include ]; then
            mkdir -p ${B}/arch/${ARCH}/include/
            cp -fR ../msm-kernel/arch/${ARCH}/include/* ${B}/arch/${ARCH}/include/
    fi

    install -d ${B}/arch/${ARCH}/boot/
    cp -R ../msm-kernel/arch/${ARCH}/boot/dts/ ${B}/arch/${ARCH}/boot/

    install -d ${B}/${KERNEL_OUTPUT_DIR}
    for typeformake in ${KERNEL_IMAGETYPE_FOR_MAKE} ; do
        install -m 0644 ${typeformake} ${B}/${KERNEL_OUTPUT_DIR}
    done
    install -m 0644 vmlinux ${B}
    install -m 0644 System.map ${B}
    # copy initramfs scripts
    install -d ${B}/usr
    cp -R ../msm-kernel/usr/gen_init_cpio ${B}/usr
    cp -R ../msm-kernel/usr/initramfs_data.cpio ${B}/usr
    cp -R ../msm-kernel/usr/initramfs_inc_data ${B}/usr

    #copy modules
    install -d ${B}/modules
    rsync -av --exclude=build --exclude=source ../staging/lib ${B}/modules
}

do_prebuilt_shared_workdir[cleandirs] += "${STAGING_KERNEL_BUILDDIR}"
do_prebuilt_shared_workdir() {
    cd ${B}

    kerneldir=${STAGING_KERNEL_BUILDDIR}
    install -d $kerneldir
    install -d $kerneldir/certs
    install -m 0644 certs/* $kerneldir/certs
    #
    # Store the kernel version in sysroots for module-base.bbclass
    #

    echo "${KERNEL_VERSION}" > $kerneldir/${KERNEL_PACKAGE_NAME}-abiversion

    # Copy files required for module builds
    install -m 0644 System.map $kerneldir/System.map-${KERNEL_VERSION}
    [ -e Module.symvers ] && install -m 0644 Module.symvers $kerneldir/
    install -m 0644 Makefile $kerneldir/
    install -m 0644 .config $kerneldir/
    mkdir -p $kerneldir/include/config
    install -m 0644 include/config/auto.conf $kerneldir/include/config/auto.conf
    install -m 0644 include/config/kernel.release $kerneldir/include/config/kernel.release
    cp -R include/generated/ $kerneldir/include/
    cp -R ${B}/scripts $kerneldir

    if [ -d arch/${ARCH}/include ]; then
            mkdir -p $kerneldir/arch/${ARCH}/include/
            cp -fR arch/${ARCH}/include/* $kerneldir/arch/${ARCH}/include/
    fi

    install -d $kerneldir/arch/${ARCH}/boot/
    cp -R arch/${ARCH}/boot/dts/ $kerneldir/arch/${ARCH}/boot/
}

do_prebuilt_install[dirs] = "${B}"
fakeroot do_prebuilt_install() {
    #
    # Install various kernel output (zImage, map file, config, module support files)
    # From prebuilt paths
    #
    install -d ${D}/${KERNEL_IMAGEDEST}
    install -d ${D}/boot
    for imageType in ${KERNEL_IMAGETYPES} ; do
        install -m 0644 ${KERNEL_OUTPUT_DIR}/${imageType} ${D}/${KERNEL_IMAGEDEST}/${imageType}-${KERNEL_VERSION}
        if [ "${KERNEL_PACKAGE_NAME}" = "kernel" ]; then
            ln -sf ${imageType}-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${imageType}
        fi
    done
    install -m 0644 System.map ${D}/boot/System.map-${KERNEL_VERSION}
    install -m 0644 .config ${D}/boot/config-${KERNEL_VERSION}
    install -m 0644 vmlinux ${D}/boot/vmlinux-${KERNEL_VERSION}
    [ -e Module.symvers ] && install -m 0644 Module.symvers ${D}/boot/Module.symvers-${KERNEL_VERSION}
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}${sysconfdir}/modprobe.d

    cp -R modules/lib ${D}
    rm ${D}/lib/modules/${KERNEL_VERSION}/modules.*.bin
    rm ${D}/lib/modules/${KERNEL_VERSION}/modules.a*
    rm ${D}/lib/modules/${KERNEL_VERSION}/modules.d*
    rm ${D}/lib/modules/${KERNEL_VERSION}/modules.s*

    # Copied files may cause host contamination due to invalid UID. Change ownership to root.
    find ${D} -name '*' -exec chown -h root:root {} \;
}

# Must be ran no earlier than after do_kernel_checkout or else Makefile won't be in ${S}/Makefile
PREBUILT_DISCARDED_TASKS += "\
    do_configure \
    do_compile \
    do_kernel_link_images \
    do_compile_kernelmodules \
    do_shared_workdir \
    do_install \
"
python () {
    if d.getVar('KERNEL_USE_PREBUILTS') == 'True':
        for task in d.getVar('PREBUILT_DISCARDED_TASKS').split():
            d.setVarFlag(task, 'noexec', '1')
        bb.build.addtask('do_prebuilt_configure', 'do_configure', 'do_prepare_recipe_sysroot', d)
        bb.build.addtask('do_prebuilt_install', 'do_install', 'do_compile', d)
        bb.build.addtask('do_prebuilt_shared_workdir', 'do_compile_kernelmodules', 'do_compile', d)
}

# append DTB
# msm kernel trees have a special treatment for DTS, and both arm and
# arm64 DTS are located in arch/arm64/boot/dts/qcom folder, which
# confuses kernel-devicetree class, so we can't use it. Instead let's
# make sure that we generate all DTBs using the kernel 'dtbs' target,
# then we can append the DTBs that we need for $MACHINE.
KERNEL_EXTRA_ARGS += "dtbs"

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
