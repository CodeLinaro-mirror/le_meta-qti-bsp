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

DEPENDS += " mkbootimg-native openssl-native kern-tools-native rsync-native"
RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"

DEPENDS_append_aarch64 = " libgcc"
KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_PRIORITY           = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS        += "O=${B}"

SRC_URI   =  "${PATH_TO_REPO}/kernel/msm-5.4/.git;protocol=${PROTO};destsuffix=kernel/msm-5.4;usehead=1 \
              ${PATH_TO_REPO}/data-kernel/.git;protocol=${PROTO};destsuffix=data-kernel;usehead=1"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "kernel_data_display_sched_ais_video"

FILESEXTRAPATHS_append := ":${THISDIR}/files"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = "capture capture-base capture-image capture-vmlinux capture-dev"

KBRANCH ?= ""
KMETA = "kernel-meta"
KMACHINE ?= "${BASEMACHINE}"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG ?= "${CAPTURE_KERNEL_CONFIG}"
LINUX_VERSION_EXTENSION = "${@['-perf', ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"

COMPATIBLE_MACHINE = "(${BASEMACHINE})"
KERNEL_IMAGEDEST = "boot"
KERNEL_IMAGETYPE = "Image"
KERNEL_PACKAGE_NAME = "capture"
KERNEL_DEVICETREE = "vendor/qcom/sa8155p-v2-adp-air-capture.dtb vendor/qcom/sa8195p-v2-adp-air-capture.dtb"

SRC_DIR   =  "${SRC_DIR_ROOT}/kernel/msm-5.4"
S         =  "${WORKDIR}/kernel/msm-5.4"
PR = "r0"

DEPENDS += "dtc-native"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'mkdtimg-native', '', d)}"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"
EXTRA_OEMAKE_append += "INSTALL_MOD_STRIP=1"
KERNEL_EXTRA_ARGS += "${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'DTC_EXT=${STAGING_BINDIR_NATIVE}/dtc CONFIG_BUILD_ARM64_DT_OVERLAY=y', '', d)}"

do_kernel_metadata_prepend() {
    set +e
    if [ -n "${KBUILD_DEFCONFIG}" ]; then
        if [ -f "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}" ]; then
            if [ -f "${WORKDIR}/defconfig" ]; then
                # If the two defconfig's are different, warn that we didn't overwrite the
                # one already placed in WORKDIR by the fetcher.
                cmp "${WORKDIR}/defconfig" "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}"
                if [ $? -ne 0 ]; then
                    bbwarn "defconfig detected in WORKDIR. ${KBUILD_DEFCONFIG} overide"
                    cp -f ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
                fi
            fi
        fi
    fi
}

do_generate_gki_defconfig() {
    bbnote "Generating GKI defconfig"

    gki_defconfig=`echo ${CAPTURE_KERNEL_CONFIG} | sed 's/vendor\///g'`

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

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

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

nand_boot_flag = "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', '1', '0', d)}"

do_shared_workdir[dirs] = "${DEPLOYDIR}"

INHIBIT_PACKAGE_STRIP = "1"
KERNEL_VERSION_SANITY_SKIP="1"
