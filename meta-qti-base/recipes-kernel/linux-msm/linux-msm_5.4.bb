inherit kernel kernel-yocto

DESCRIPTION = "CAF Linux Kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

python __anonymous () {
  # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
  # to kernel.bbclass. We override the variable as msm kernel can't
  # support alternate image builds
  if d.getVar("KERNEL_IMAGETYPE", True):
      d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "")
}

KERNEL_PATH = "${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}"

python do_patch_uncompressed_kernel () {
    if (d.getVar('KERNEL_IMAGETYPE', True) == "Image"):
        import struct
        kernel = d.getVar('KERNEL_PATH')
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

addtask do_patch_uncompressed_kernel after do_install before do_deploy

DEPENDS += " mkbootimg-native openssl-native kern-tools-native rsync-native"
RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"

DEPENDS_append_aarch64 = " libgcc"
KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_PRIORITY           = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS        += "O=${B}"
KERNEL_EXTRA_ARGS_append_sa8155  += "TARGET_BOARD_TYPE=auto"
KERNEL_EXTRA_ARGS_append_sa6155  += "TARGET_BOARD_TYPE=auto"

SRC_URI   =  "${PATH_TO_REPO}/kernel/msm-5.4/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4;usehead=1 \
              ${PATH_TO_REPO}/kernel/msm-5.4/techpack/display/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4/techpack/display;usehead=1 \
              ${PATH_TO_REPO}/kernel/msm-5.4/techpack/sched/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4/techpack/sched;usehead=1 \
              ${PATH_TO_REPO}/kernel/msm-5.4/techpack/ais/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4/techpack/ais;usehead=1 \
              ${PATH_TO_REPO}/kernel/msm-5.4/techpack/video/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4/techpack/video;usehead=1"

SRC_URI_append = "  \
              ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' file://systemd.cfg', '', d)} \
	     "
SRC_URI_append =  "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', ' file://weston.cfg', '', d)}"

SRC_URI_append =  " file://lxc.cfg"
SRC_URI_append =  " file://ipc.cfg"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "kernel_data_display_sched_ais_video"

FILESEXTRAPATHS_append := ":${THISDIR}/files"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"

KBRANCH ?= ""
KMETA = "kernel-meta"
KMACHINE ?= "${BASEMACHINE}"
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

do_kernel_checkout[noexec] = "1"

# extra task for configuration checks
addtask kernel_configcheck after do_configure before do_compile

do_compile () {
    oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
}

do_deploy() {
    if [[ ${KERNEL_IMAGETYPE} != *-dtb ]]; then
        bberror "${PN}: Only appended DTB supported; Change KERNEL_IMAGETYPE to ${KERNEL_IMAGETYPE}-dtb in your kernel config."
        return
    fi

    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi
}

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

COMPATIBLE_MACHINE = "(${BASEMACHINE})"
KERNEL_IMAGEDEST = "boot"

SRC_DIR   =  "${SRC_DIR_ROOT}/kernel/msm-5.4"
S         =  "${WORKDIR}/kernel/msm-5.4"
PR = "r0"

DEPENDS += "dtc-native"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'mkdtimg-native', '', d)}"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"
EXTRA_OEMAKE_append += "INSTALL_MOD_STRIP=1"
KERNEL_EXTRA_ARGS += "${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'DTC_EXT=${STAGING_BINDIR_NATIVE}/dtc CONFIG_BUILD_ARM64_DT_OVERLAY=y', '', d)}"

do_shared_workdir_append () {
        cp Makefile $kerneldir/
        cp -fR usr $kerneldir/

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

        # Generate kernel headers
        oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}
}

do_deploy_append() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
        if ${@bb.utils.contains('COMBINED_FEATURES', 'qti-lxc', 'true', 'false', d)}; then
            ${STAGING_BINDIR_NATIVE}/mkdtimg create ${DEPLOYDIR}/${PRODUCT}-dtbo.img ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*lxc-overlay.dtbo
        else
            rm ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*lxc-overlay.dtbo
            ${STAGING_BINDIR_NATIVE}/mkdtimg create ${DEPLOYDIR}/${PRODUCT}-dtbo.img ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*.dtbo
        fi
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', 'true', 'false', d)}; then
        cp -f ${B}/arch/${ARCH}/boot/Image ${DEPLOYDIR}/linux-lv.img
        cp -f ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*.dtb ${DEPLOYDIR}/
    fi
}

nand_boot_flag = "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', '1', '0', d)}"

do_deploy () {
    extra_mkbootimg_params=""
    if [ ${nand_boot_flag} == "1" ]; then
        extra_mkbootimg_params='--tags-addr ${KERNEL_TAGS_OFFSET}'
    fi

    cat ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ${B}/arch/${ARCH}/boot/dts/vendor/qcom/*.dtb > ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-dtb-${KERNEL_VERSION}

    # Make bootimage
    ${STAGING_BINDIR_NATIVE}/mkbootimg --kernel ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-dtb-${KERNEL_VERSION} \
        --ramdisk /dev/null \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        ${extra_mkbootimg_params} --output ${DEPLOYDIR}/${BOOTIMAGE_TARGET}
    # Copy vmlinux and zImage into deplydir for boot.img creation
    install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}
    install -m 0644 vmlinux ${DEPLOYDIR}
}

inherit qsigning

#Sign boot image after generation
do_deploy[postfuncs] += "sign_bootimg"

do_shared_workdir[dirs] = "${DEPLOYDIR}"

INHIBIT_PACKAGE_STRIP = "1"
KERNEL_VERSION_SANITY_SKIP = "1"
