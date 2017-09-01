inherit kernel qperf

DESCRIPTION = "QuIC Linux Kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "(apq8098|8x96auto|8x96autofusion|8x96autonapier|8x96autocv2x)"

# Default image type is zImage, change it in machine conf if needed.
KERNEL_IMAGETYPE ?= "zImage"

python __anonymous () {
  if bb.utils.contains('DISTRO_FEATURES', 'qti-perf', True, False, d):
      imgtype = d.getVar("KERNEL_PERF_IMAGETYPE", True)
      if imgtype:
          d.setVar("KERNEL_IMAGETYPE", d.getVar("KERNEL_PERF_IMAGETYPE", True))
      perfconf = d.getVar("KERNEL_PERF_DEFCONFIG", True)
      if perfconf:
          d.setVar("KERNEL_CONFIG", d.getVar("KERNEL_PERF_DEFCONFIG", True))
          d.appendVar("SRC_URI", " file://0001-automotive-add-boot-marker.patch")
          d.appendVar("SRC_URI", " file://0002-driver-core-remove-lock-for-platform-devices.patch")
      perfcmd = d.getVar("KERNEL_PERF_CMD_PARAMS", True)
      if perfcmd:
          d.setVar("KERNEL_CMD_PARAMS", d.getVar("KERNEL_PERF_CMD_PARAMS", True))
  else:
      d.setVar("KERNEL_CONFIG", d.getVar("KERNEL_DEFCONFIG", True))

  # add early_init to DISTRO_FEATURES to use early user space feature
  if bb.utils.contains('DISTRO_FEATURES', 'early_init', True, False, d):
      d.appendVar("SRC_URI", " file://0003-init-early-user-space.patch")

  if bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', True, False, d):
      d.appendVar("SRC_URI", " file://0001-ARM-dts-msm-Add-phy-speed-mode-properties.patch")
      d.appendVar("SRC_URI", " file://0001-ethernet-ethernet-support-early-ethernet-feature.patch")

  # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
  # to kernel.bbclass. We override the variable as msm kernel can't
  # support alternate image builds
  if d.getVar("KERNEL_IMAGETYPE", True):
      d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "")
}

KERNEL_IMAGEDEST = "boot"

DEPENDS_append_aarch64 = " libgcc"
KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_PRIORITY           = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS        += "O=${B}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   =  "file://kernel"

SRC_DIR   =  "${WORKSPACE}/kernel/msm-4.4"
S         =  "${WORKDIR}/kernel/msm-4.4"
GITVER    =  "${@base_get_metadata_git_revision('${SRC_DIR}',d)}"
PV = "git"
PR = "r5"

DEPENDS += "dtbtool-native mkbootimg-native packkernelimg"
DEPENDS += "mkbootimg-native dtc-native"
PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"
RDEPENDS_kernel-base = ""

KERNEL_DTB_LIST = "${@d.getVar('KERNEL_DEVICETREE', True).replace('qcom/', '')}"

do_compile_prepend() {
    mkdir -p "${B}/arch/${ARCH}/boot/dts/qcom"
}

# Put the zImage in the kernel-dev pkg
FILES_kernel-dev += "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}"

# Additional defconfigs for systemd
do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_DEVTMPFS_MOUNT=y
CONFIG_FHANDLE=y
KERNEL_EXTRACONFIGS
}

do_patch_append () {
    if bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d):
        bb.build.exec_func('do_defconfig_patch',d)
}

do_configure () {
    oe_runmake_call -C ${S} ARCH=${ARCH} ${KERNEL_EXTRA_ARGS} ${KERNEL_CONFIG}
}

do_shared_workdir () {
        cd ${B}

        kerneldir=${STAGING_KERNEL_BUILDDIR}
        install -d $kerneldir

        #
        # Store the kernel version in sysroots for module-base.bbclass
        #

        echo "${KERNEL_VERSION}" > $kerneldir/kernel-abiversion

        # Copy files required for module builds
        cp System.map $kerneldir/System.map-${KERNEL_VERSION}
        cp Module.symvers $kerneldir/Module.symvers
        cp Makefile $kerneldir/
        cp .config $kerneldir/
        cp -fR usr $kerneldir/

        # Signing keys may not be present
        mkdir -p $kerneldir/certs
        cp certs/signing_key.pem $kerneldir/certs/signing_key.pem
        cp certs/signing_key.x509 $kerneldir/certs/signing_key.x509

        # include/config
        mkdir -p $kerneldir/include/config
        cp include/config/kernel.release $kerneldir/include/config/kernel.release
        cp include/config/auto.conf $kerneldir/include/config/auto.conf

        # We can also copy over all the generated files and avoid special cases
        # like version.h, but we've opted to keep this small until file creep starts
        # to happen
        if [ -e include/linux/version.h ]; then
                mkdir -p $kerneldir/include/linux
                cp include/linux/version.h $kerneldir/include/linux/version.h
        fi

        mkdir -p $kerneldir/include/generated/
        cp -fR include/generated/* $kerneldir/include/generated/

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
                scripts/sign-file \
                scripts/sortextable;
            do
                if [ -e $i ]; then
                    mkdir -p $kerneldir/`dirname $i`
                    cp $i $kerneldir/$i
                fi
            done
        fi

        cp ${STAGING_KERNEL_DIR}/scripts/gen_initramfs_list.sh $kerneldir/scripts/

        # Make vmlinux available as soon as possible
        if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-perf', 'true', 'false', d)}; then
                install -d ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}
                install -m 0644 ${KERNEL_OUTPUT} ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
                install -m 0644 vmlinux ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}/vmlinux-${KERNEL_VERSION}
                install -m 0644 vmlinux ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}/vmlinux
        else
                install -d ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}
                install -m 0644 ${KERNEL_OUTPUT} ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
                install -m 0644 vmlinux ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}/vmlinux-${KERNEL_VERSION}
                install -m 0644 vmlinux ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}/vmlinux
        fi
}

do_install_append() {
    oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}
}


do_deploy_prepend() {

    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi
}

do_deploy () {

    extra_mkbootimg_params=""
    if [ ${nand_boot_flag} == "1" ]; then
        extra_mkbootimg_params='--dt ${D}/${KERNEL_IMAGEDEST}/masterDTB --tags-addr ${KERNEL_TAGS_OFFSET}'
    fi

    mkdir -p ${DEPLOY_DIR_IMAGE}
    if [ ${KERNEL_IMAGETYPE} == "Image" ]; then
        packkernelimg --kernel "${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}" --dt "${B}/arch/${ARCH}/boot/dts/qcom" --dt_list "${KERNEL_DTB_LIST}"
    fi

    # Make bootimage
    ${STAGING_BINDIR_NATIVE}/mkbootimg --kernel ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} \
        --ramdisk /dev/null \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        ${extra_mkbootimg_params} --output ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img
    cp ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} "${DEPLOY_DIR_IMAGE}/"
    cp ${B}/vmlinux "${DEPLOY_DIR_IMAGE}/"
}

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'linux-msm-4.4_auto', 'none',d)}"
include ${INCSUFFIX}.inc

