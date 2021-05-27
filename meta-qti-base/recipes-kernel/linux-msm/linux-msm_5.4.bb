SUMMARY = "CAF Linux Kernel"
DESCRIPTION = "CAF Linux Kernel for QTI MSM SoC"
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "\
    dtc-native kern-tools-native  mkbootimg-native \
    ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'mkdtimg-native', '', d)} \
    openssl-native rsync-native \
"
DEPENDS_append_aarch64 = " libgcc"

KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/msm-5.4/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4;usehead=1 \
    ${PATH_TO_REPO}/kernel/msm-5.4/techpack/display/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4/techpack/display;usehead=1 \
    ${PATH_TO_REPO}/kernel/msm-5.4/techpack/ais/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4/techpack/ais;usehead=1 \
    ${PATH_TO_REPO}/kernel/msm-5.4/techpack/video/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4/techpack/video;usehead=1 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' file://systemd.cfg', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', ' file://weston.cfg', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', ' file://dual-wlan.cfg', \
        bb.utils.contains('MACHINE_FEATURES', 'qti-wlan', ' file://wlan.cfg', '', d), d)} \
    file://lxc.cfg \
    file://ipc.cfg \
"
SRC_URI_append_qtiquingvm8295 = " file://qtiquingvm8295.cfg"
SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "kernel_data_display_ais_video"

inherit kernel kernel-yocto qsigning ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

S = "${WORKDIR}/kernel/msm-5.4"

EXTRA_OEMAKE += "INSTALL_MOD_STRIP=1"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"
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

KERNEL_PRIORITY = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS_append = " O=${B}"
KERNEL_EXTRA_ARGS_append_sa8155 = " TARGET_BOARD_TYPE=auto"
KERNEL_EXTRA_ARGS_append_sa6155 = " TARGET_BOARD_TYPE=auto"
KERNEL_EXTRA_ARGS_append = " ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'DTC_EXT=${STAGING_BINDIR_NATIVE}/dtc CONFIG_BUILD_ARM64_DT_OVERLAY=y', '', d)}"

KBRANCH ?= ""
KMETA = "kernel-meta"
KMACHINE ?= "${BASEMACHINE}"
COMPATIBLE_MACHINE = "(${BASEMACHINE})"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG ?= "${KERNEL_CONFIG}"
LINUX_VERSION_EXTENSION = "${@['-perf', ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"

do_kernel_metadata_prepend() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'kdump-support', 'true', 'false', d)}; then
        set +e
        if [ -n "${KBUILD_DEFCONFIG}"  ]; then
            if [ -f "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}"  ]; then
                if [ -f "${WORKDIR}/defconfig"  ]; then
                    # If the two defconfig's are different, warn that we didn't overwrite the
                    # one already placed in WORKDIR by the fetcher.
                    cmp "${WORKDIR}/defconfig" "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}"
                    if [ $? -ne 0  ]; then
                        bbwarn "defconfig detected in WORKDIR. ${KBUILD_DEFCONFIG} overide"
                        cp -f ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
                    fi
                fi
            fi
        fi
    fi
}

do_generate_gki_defconfig() {
    bbnote "Generating GKI defconfig"

    gki_defconfig=`echo ${KERNEL_CONFIG} | sed 's/vendor\///g'`

    # FIXME: Workaround for executing generate_defconfig.sh
    LD=`echo ${LD} | sed 's/--sysroot.*//g'`

    ${S}/scripts/gki/generate_defconfig.sh ${gki_defconfig}
}

addtask do_generate_gki_defconfig after do_unpack before do_kernel_metadata
do_generate_gki_defconfig[depends] += "virtual/${TARGET_PREFIX}binutils:do_populate_sysroot"
do_generate_gki_defconfig[depends] += "virtual/${TARGET_PREFIX}binutils:do_prepare_recipe_sysroot"

do_kernel_checkout[noexec] = "1"

do_compile () {
    oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
}

do_shared_workdir[dirs] = "${DEPLOYDIR}"
do_shared_workdir_append () {
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
}

do_deploy () {
    # Copy Kernel scripts to deploydir
    install -d ${DEPLOYDIR}/build-artifacts
    install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
    cp  ${STAGING_KERNEL_DIR}/usr/gen_initramfs_list.sh ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts

    # Copy Image appended with dtbs to deploydir
    cat ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*.dtb > ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-dtb-${KERNEL_VERSION}

    # Make bootimage
    ${STAGING_BINDIR_NATIVE}/mkbootimg --kernel ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-dtb-${KERNEL_VERSION} \
        --ramdisk /dev/null \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        --output ${DEPLOYDIR}/${BOOTIMAGE_TARGET}
    # Copy vmlinux and zImage into deploydir for boot.img creation
    install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}
    install -m 0644 ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-dtb-${KERNEL_VERSION} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}-dtb
    install -m 0644 vmlinux ${DEPLOYDIR}

    if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
        ${STAGING_BINDIR_NATIVE}/mkdtimg create ${DEPLOYDIR}/${PRODUCT}-dtbo.img ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*.dtbo
    fi

    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        cp -f ${B}/arch/${ARCH}/boot/Image ${DEPLOYDIR}/linux-lv.img
        cp -f ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*.dtb ${DEPLOYDIR}/
    fi
}

#Sign boot image after generation
do_deploy[postfuncs] += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor','', 'sign_bootimg', d)}"

PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"

INHIBIT_PACKAGE_STRIP = "1"
KERNEL_VERSION_SANITY_SKIP = "1"

RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""
