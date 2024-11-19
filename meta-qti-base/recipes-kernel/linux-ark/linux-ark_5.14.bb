SUMMARY = "ARK Linux Kernel"
DESCRIPTION = "ARK Linux Kernel for QTI SoC"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

RH_SRC = "${SRC_DIR_ROOT}/kernel/${RH_KERNEL_NAME}"
PATCH_DIR = "${SRC_DIR_ROOT}/meta-qti-bsp/meta-qti-base/recipes-kernel/linux-ark/files/"

DEPENDS += "\
    dtc-native elfutils-native kern-tools-native \
    mkbootimg-native openssl-native pahole-native rsync-native \
"
DEPENDS:append:aarch64 = " libgcc"

KERNEL_CC:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/${RH_KERNEL_NAME}/.git;protocol=${PROTO};name=kernel;destsuffix=kernel/${RH_KERNEL_NAME};usehead=1 \
    file://dm.cfg \
    ${@bb.utils.contains_any('VARIANT', 'perf user', 'file://perf.cfg', '', d)} \
    file://nr_cpus.cfg \
    file://usb_adb.cfg \
    file://wlan.cfg \
    file://pan.cfg \
    ${@bb.utils.contains_any('VARIANT', 'perf user', '', 'file://devmem.cfg', d)} \
"

SRCREV_kernel = "${AUTOREV}"

inherit kernel kernel-yocto qsigning ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

S = "${WORKDIR}/kernel/${RH_KERNEL_NAME}"

EXTRA_OEMAKE += "INSTALL_MOD_STRIP=1 --include-dir=${S}"

LDFLAGS:aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"

python __anonymous () {
  # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
  # to kernel.bbclass. We override the variable as msm kernel can't
  # support alternate image builds
  if d.getVar("KERNEL_IMAGETYPE", True):
      d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "")
}

BOOT_IMAGE_PATH = "${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}"

python do_uncompressed_kernel_patch () {
    if (d.getVar('KERNEL_IMAGETYPE', True) == "Image"):
        import struct
        kernel = d.getVar('BOOT_IMAGE_PATH')
        f = open(kernel, 'rb')
        KERNEL_TYPE = 'UNCOMPRESSED_IMG'.encode()
        tmp=open(kernel + "-tmp", 'wb')
        tmp.write(struct.pack('16s', KERNEL_TYPE))
        tmp.write(struct.pack('1I', os.fstat(f.fileno()).st_size))
        tmp.write(f.read())
        f.close()
        tmp.close()
        f = open(kernel, 'wb')
        tmp=open(kernel + "-tmp", 'rb')
        f.write(tmp.read())
        f.close()
        tmp.close()
        os.remove(kernel + "-tmp")
}

addtask do_uncompressed_kernel_patch after do_install before do_deploy

do_rh_config[depends] += "flex-native:do_populate_sysroot"
do_rh_config[depends] += "bison-native:do_populate_sysroot"

do_rh_config () {
    make -C ${RH_SRC}/redhat ARCH=arm64 dist-configs
    cp ${RH_SRC}/redhat/configs/kernel-automotive-5.14.0-aarch64.config ${S}/arch/arm64/configs/defconfig
    rm -rf ${RH_SRC}/.config ${RH_SRC}/include/config/ \
    ${RH_SRC}/include/generated/ ${RH_SRC}/arch/$ARCH/include/generated/
    make -C ${S} O=${B} CROSS_COMPILE="" defconfig
    make -C ${S} O=${B} CROSS_COMPILE="" savedefconfig
    cp ${B}/defconfig ${S}/arch/arm64/configs/defconfig
}
addtask do_rh_config after do_unpack before do_kernel_metadata

KERNEL_PRIORITY = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS:append = " O=${B}"

EXTRA_OEMAKE:remove = "PAHOLE=false"
KCONFIG_CONFIG_COMMAND:remove = "PAHOLE=false"

KBRANCH ?= ""
KMETA = "kernel-meta"
KMACHINE ?= "${BASEMACHINE}"
COMPATIBLE_MACHINE = "(${BASEMACHINE})"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG ?= "${KERNEL_CONFIG}"
LINUX_VERSION_EXTENSION = "${@['-perf', ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"

do_kernel_checkout[noexec] = "1"
do_validate_branches[noexec] = "1"

do_compile:prepend() {
    export DTC_FLAGS="-@"
}

do_compile () {
    oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
}

do_shared_workdir[dirs] = "${DEPLOYDIR}"
do_shared_workdir:append () {
        cp include/config/auto.conf $kerneldir/include/config/auto.conf

        if [ -d arch/${ARCH}/include ]; then
                mkdir -p $kerneldir/arch/${ARCH}/include/
                cp -fR arch/${ARCH}/include/* $kerneldir/arch/${ARCH}/include/
        fi

        if [ -d arch/${ARCH}/boot ]; then
                mkdir -p $kerneldir/arch/${ARCH}/boot/
                cp -fR arch/${ARCH}/boot/* $kerneldir/arch/${ARCH}/boot/
        fi

        if [ -d scripts ]; then
            for i in \
                scripts/unifdef \
                scripts/basic/bin2c \
                scripts/basic/fixdep \
                scripts/conmakehash \
                scripts/dtc/dtc \
                scripts/kallsyms \
                scripts/kconfig/conf \
                scripts/mod/mk_elfconfig \
                scripts/mod/modpost \
                scripts/recordmcount \
                scripts/sign-file \
                scripts/sortextable;
            do
                if [ -e $i ]; then
                    mkdir -p $kerneldir/`dirname $i`
                    cp $i $kerneldir/$i
                fi
            done
        fi

        if [ -f usr/gen_init_cpio ]; then
            mkdir -p $kerneldir/usr/
            cp -f usr/gen_init_cpio $kerneldir/usr/
        fi
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

KERNEL_MACHINE_DTB ?= ""

do_deploy () {
    # Copy Kernel scripts to deploydir
    install -d ${DEPLOYDIR}/build-artifacts
    install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
    install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr

    if [ -f ${STAGING_KERNEL_BUILDDIR}/usr/gen_init_cpio ]; then
        cp  ${STAGING_KERNEL_BUILDDIR}/usr/gen_init_cpio ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr
    fi

    # Copy Image and dtbs to deploydir
    install -m 0644 vmlinux ${DEPLOYDIR}

    if [ "${KERNEL_IMAGE_HEADER_VERSION}" = "2" ]; then
        cp ${B}/arch/arm64/boot/Image ${D}/${KERNEL_IMAGEDEST}/Image
        install -m 0644 ${D}/${KERNEL_IMAGEDEST}/Image ${DEPLOYDIR}
        if [ -n "${KERNEL_BASE_DTB}" ]; then
            install -d ${DEPLOYDIR}/build-artifacts/kernel-dtb

            for dtb in ${KERNEL_BASE_DTB}; do
                if [ -f ${B}/$dtb ]; then
                    install -m 0644 ${B}/$dtb ${DEPLOYDIR}/build-artifacts/kernel-dtb
                fi
            done
        fi
    elif [ "${KERNEL_IMAGE_HEADER_VERSION}" = "1" ]; then
        cat ${B}/arch/arm64/boot/Image.gz \
            ${B}/arch/arm64/boot/dts/qcom/${KERNEL_MACHINE_DTB} > ${D}/${KERNEL_IMAGEDEST}/Image.gz-dtb
        install -m 0644 ${D}/${KERNEL_IMAGEDEST}/Image.gz-dtb ${DEPLOYDIR}
    else
        echo "Unknown Boot Image Header Version"
        return 1
    fi
}

#PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"

INHIBIT_PACKAGE_STRIP = "1"
KERNEL_VERSION_SANITY_SKIP = "1"
KERNEL_IMAGETYPE_FOR_MAKE += "dtbs"
KERNEL_IMAGETYPE_FOR_MAKE += "${KERNEL_IMAGETYPE}"
KERNEL_IMAGETYPE_FOR_MAKE += "modules"

do_compile_kernelmodules[noexec] = "1"
