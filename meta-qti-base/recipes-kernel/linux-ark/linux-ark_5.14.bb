SUMMARY = "Linux Kernel"
DESCRIPTION = "Linux Kernel for QTI MSM SoC"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

MY_SRC = "${SRC_DIR_ROOT}/kernel/rh-kernel-5.14"
PATCH_DIR = "${SRC_DIR_ROOT}/meta-qti-bsp/meta-qti-base/recipes-kernel/linux-ark/files/"
MY_WDIR = "${WORKDIR}/kernel/rh-kernel-5.14"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEPENDS += "\
    dtc-native kern-tools-native  mkbootimg-native \
    ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'mkdtimg-native', '', d)} \
    openssl-native rsync-native \
    flex-native \
"
DEPENDS:append:aarch64 = " libgcc"

KERNEL_CC:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/rh-kernel-5.14/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4;usehead=1 \
"
SRC_URI += "file://defconfig \
			file://0001-centos-5.14-Fix-to-bypass-redhad-env.patch \
            file://0002-centos-5.14-build-fixes-while-porting-from-5.4.patch \
            file://0001-defconfig-add-overrides-to-resolve-build-error.patch \
            file://0001-redhat-HACK-remove-rpm-build-dependency.patch \
"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "kernel_data_display_ais_video"

inherit kernel kernel-yocto qsigning ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

S = "${WORKDIR}/kernel/rh-kernel-5.14"

EXTRA_OEMAKE += "INSTALL_MOD_STRIP=1"

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

do_rh_config () {
    make -C ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/redhat  ARCH=arm64 dist-configs
    cp ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/redhat/configs/kernel-automotive-5.14.0-aarch64.config ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/arch/arm64/configs/defconfig
    make -C ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14 CROSS_COMPILE="" defconfig
    make -C ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14 CROSS_COMPILE="" savedefconfig
    cp ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/defconfig ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/arch/arm64/configs/defconfig
    cp ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/defconfig ${SRC_DIR_ROOT}/meta-qti-bsp/meta-qti-base/recipes-kernel/linux-ark/files/defconfig
    rm -rf ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/.config ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/include/config/ \
    ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/include/generated/ ${SRC_DIR_ROOT}/kernel/rh-kernel-5.14/arch/$ARCH/include/generated/
}
addtask rh_config after do_prepare_recipe_sysroot before do_unpack

do_patch_config() {
     do_patch_config_call() {
         cd ${MY_SRC}
         patch -f -p1 < ${PATCH_DIR}/0001-defconfig-add-overrides-to-resolve-build-error.patch
         patch -f -p1 < ${PATCH_DIR}/0001-redhat-HACK-remove-rpm-build-dependency.patch
    }

    do_patch_config_call || bbwarn "do_patch_config_call failed"

}
addtask patch_config after do_fetch before do_rh_config

do_patch_more() {
    cd ${MY_WDIR}
    patch -f -p1 < ${WORKDIR}/0001-centos-5.14-Fix-to-bypass-redhad-env.patch
    patch -f -p1 < ${WORKDIR}/0002-centos-5.14-build-fixes-while-porting-from-5.4.patch
}
addtask patch_more after do_unpack before do_kernel_metadata

KERNEL_PRIORITY = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS:append = " O=${B}"
KERNEL_EXTRA_ARGS:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'DTC_EXT=${STAGING_BINDIR_NATIVE}/dtc CONFIG_BUILD_ARM64_DT_OVERLAY=y', '', d)}"

KBRANCH ?= ""
KMETA = "kernel-meta"
KMACHINE ?= "${BASEMACHINE}"
COMPATIBLE_MACHINE = "(${BASEMACHINE})"
KCONFIG_MODE = "--alldefconfig"
#KBUILD_DEFCONFIG ?= "${KERNEL_CONFIG}"
LINUX_VERSION_EXTENSION = "${@['-perf', ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"

do_kernel_metadata:prepend() {
    set +e
    if [ -n "${KBUILD_DEFCONFIG}"  ]; then
        if [ -f "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}"  ]; then
            if [ -f "${WORKDIR}/defconfig"  ]; then
                # If the two defconfig's are different, warn that we overwrote the
                # one already placed in WORKDIR.
                cmp "${WORKDIR}/defconfig" "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}"
                if [ $? -ne 0  ]; then
                    bbwarn "defconfig detected in WORKDIR. ${KBUILD_DEFCONFIG} copied over it"
                    cp -f ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
                fi
            fi
        fi
    fi
}

do_kernel_checkout[noexec] = "1"
do_validate_branches[noexec] = "1"

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

        cp ${STAGING_KERNEL_DIR}/usr/gen_initramfs_list.sh $kerneldir/scripts/
        if [ -f usr/gen_init_cpio ]; then
            mkdir -p $kerneldir/usr/
            cp -f usr/gen_init_cpio $kerneldir/usr/
        fi
}

do_deploy () {
    # Copy Kernel scripts to deploydir
    install -d ${DEPLOYDIR}/build-artifacts
    install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
    install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr
    cp  ${STAGING_KERNEL_DIR}/usr/gen_initramfs_list.sh ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
    cp  ${STAGING_KERNEL_BUILDDIR}/usr/gen_init_cpio ${DEPLOYDIR}/build-artifacts/kernel_scripts/usr

    # Copy Image and dtbs to deploydir

    if [ "${BASEMACHINE}" = "sa8775" ]; then
    cp ${B}/arch/arm64/boot/Image ${D}/${KERNEL_IMAGEDEST}/Image
    cp ${B}/arch/arm64/boot/dts/qcom/lemans.dtb ${D}/${KERNEL_IMAGEDEST}/lemans.dtb
    # Make bootimage
    ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py --header_version ${KERNEL_IMAGE_HEADER_VERSION} \
	--kernel  ${D}/${KERNEL_IMAGEDEST}/Image \
	--dtb  ${D}/${KERNEL_IMAGEDEST}/lemans.dtb \
	--ramdisk /dev/null \
        --pagesize ${PAGE_SIZE} \
	--base ${KERNEL_BASE} \
	--ramdisk_offset 0x0 \
        --cmdline "console=ttyMSM0,115200,n8 no_console_suspend=1 androidboot.hardware=qcom androidboot.console=ttyMSM0 lpm_levels.sleep_disabled=1 msm_rtb.filter=0x237 earlycon=qcom_geni,0xa8c000 fips=0 notests nokaslr ignore_loglevel firmware_class.path=/firmware" \
	--output  ${DEPLOYDIR}/sa8775p-boot-5.14.img
    cp ${DEPLOYDIR}/sa8775p-boot-5.14.img ${DEPLOYDIR}/sa8775-boot.img
    else
    cat ${B}/arch/arm64/boot/Image.gz \
        ${B}/arch/arm64/boot/dts/qcom/sa8540p-adp-ride.dtb > ${D}/${KERNEL_IMAGEDEST}/Image.gz-dtb
    # Make bootimage
    ${STAGING_BINDIR_NATIVE}/mkbootimg --kernel ${D}/${KERNEL_IMAGEDEST}/Image.gz-dtb \
	--kernel  ${D}/${KERNEL_IMAGEDEST}/Image.gz-dtb \
	--ramdisk /dev/null \
        --pagesize ${PAGE_SIZE} \
	--base ${KERNEL_BASE} \
	--ramdisk_offset 0x0 \
        --cmdline "console=ttyMSM0,115200,n8 no_console_suspend=1 androidboot.hardware=qcom androidboot.console=ttyMSM0 lpm_levels.sleep_disabled=1 msm_rtb.filter=0x237 earlycon=qcom_geni,0x884000 fips=0 notests nokaslr ignore_loglevel firmware_class.path=/firmware" \
	--output  ${DEPLOYDIR}/sa8540p-boot-5.14.img
    fi

    if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
        ${STAGING_BINDIR_NATIVE}/mkdtimg  create ${DEPLOYDIR}/${PRODUCT}-dtbo.img ${B}/arch/${ARCH}/boot/dts/qcom/sa8295p-adp-overlay.dtbo
    fi

}

#PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"

INHIBIT_PACKAGE_STRIP = "1"
KERNEL_VERSION_SANITY_SKIP = "1"

