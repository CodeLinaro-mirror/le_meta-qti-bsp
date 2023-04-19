SUMMARY = "edk2"
DESCRIPTION = "UEFI bootloader"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-2-Clause & BSD-3-Clause"
LIC_FILES_CHKSUM = "\
    file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f \
    file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
"
PROVIDES = "virtual/bootloader"

DEPENDS += "util-linux-native"

TOOLCHAIN = "clang"

PR = "r1"
PV = "4.0"

EDK2_USE_PREBUILTS ?= "False"
KERNEL_ARCH ?= "auto"

FILESPATH =+ "${SRC_DIR_ROOT}/kernel:"
EDK2_VARIANT = "${@bb.utils.contains_any('VARIANT', 'perf user', 'perf_', 'debug_', d)}"
SRC_URI = " \
           ${PATH_TO_REPO}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/bootable/bootloader/edk2/.git;protocol=${PROTO};destsuffix=kernel-${PREFERRED_VERSION_linux-msm}/kernl_platform/bootable/bootloader/edk2;usehead=1 \
           ${@bb.utils.contains('EDK2_USE_PREBUILTS', 'True', 'file://kernel-${PREFERRED_VERSION_linux-msm}/out/msm-kernel-${KERNEL_ARCH}-${EDK2_VARIANT}defconfig/dist/', '', d)} \
          "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/bootable/bootloader/edk2"

inherit deploy

VBLE = "${@bb.utils.contains('DISTRO_FEATURES', 'vble','1', '0', d)}"
VERITY_ENABLED = "${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity','1', '0', d)}"
EARLY_ETH = "${@bb.utils.contains('DISTRO_FEATURES', 'qti-early-eth', '1', '0', d)}"
HIBERNATION = "${@bb.utils.contains('COMBINED_FEATURES', 'hibernation', '1', '0', d)}"
AB_BOOT_LXC = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-lxc', '1', '0', d)}"

EXTRA_OEMAKE = "'CLANG_BIN=${STAGING_BINDIR_NATIVE}/'\
                'CLANG_PREFIX=${STAGING_BINDIR_NATIVE}/${TARGET_SYS}/${TARGET_PREFIX}/'\
                'TARGET_ARCHITECTURE=${TARGET_ARCH}'\
                'BUILDDIR=${S}'\
                'BOOTLOADER_OUT=${S}/out'\
                'ENABLE_LE_VARIANT=true'\
                'VERIFIED_BOOT_LE=${VBLE}'\
                'VERITY_LE=${VERITY_ENABLED}'\
                'INIT_BIN_LE=/sbin/init'\
                'EDK_TOOLS_PATH=${S}/BaseTools'\
                'BOOTIMAGE_LOAD_VERIFY_IN_PARALLEL=1' \
                'EARLY_ETH_ENABLED=${EARLY_ETH}'\
                'EARLY_ETH_AS_DLKM=1' \
                'UBSAN_UEFI_GCC_FLAG_ALIGNMENT=-Wno-misleading-indentation' \
                'TARGET_BOARD_TYPE_AUTO=1' \
                'SUPPORT_AB_BOOT_LXC=${AB_BOOT_LXC}' \
                ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'VERIFIED_BOOT_ENABLED=1', '', d)} \
                ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'VERIFIED_BOOT_2=1', '', d)} \
                'EXTRA_TARGET_OPTFLAGS=--sysroot=${STAGING_DIR_TARGET}'"

EXTRA_OEMAKE:append:sa81x5 = " 'AB_RETRYCOUNT_DISABLE=1' \
                               'ENABLE_LV_ATOMIC_AB=1' "

do_prebuilt_configure() {
    cd ${WORKDIR}/kernel-${PREFERRED_VERSION_linux-msm}/out/msm-kernel-${KERNEL_ARCH}-${EDK2_VARIANT}defconfig/dist/

    install -m 0644 unsigned_abl_user*.elf ${S}/../unsigned_abl.elf
}

do_configure[noexec] = "1"
do_compile () {
    export BUILD_CC=${STAGING_BINDIR_NATIVE}/clang
    export BUILD_CXX=${STAGING_BINDIR_NATIVE}/clang++

    if ${@bb.utils.contains('MACHINE_FEATURES', 'goldcore-boot', 'true', 'false', d)}; then
        export TARGET_LINUX_BOOT_CPU_SELECTION=true
        export TARGET_LINUX_BOOT_CPU_ID=7
    fi
    oe_runmake -f makefile all
}

python () {
    if d.getVar('EDK2_USE_PREBUILTS') == 'True':
        d.setVarFlag('do_compile', 'noexec', '1')
        bb.build.addtask('do_prebuilt_configure', 'do_configure', 'do_unpack', d)
}

do_install() {
    install -d ${D}/boot
}

do_deploy() {
    if [ -f ${D}/boot/${PRODUCT}-abl.elf ]; then
      install -m 0644 ${D}/boot/${PRODUCT}-abl.elf ${DEPLOYDIR}
    else
      install -m 0644 ${S}/../unsigned_abl.elf ${DEPLOYDIR}
    fi
}
do_deploy[dirs] = "${S} ${DEPLOYDIR}"
do_deploy[nostamp] = "1"

addtask deploy before do_build after do_install

INCSUFFIX = "${@bb.utils.contains('QTI_BASE_PROP', "Y", 'edk2', 'none',d)}"
include ${INCSUFFIX}.inc

BUILD_OS = "linux"

INSANE_SKIP:${PN} = "arch"

PACKAGE_STRIP = "no"
PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "/boot"
FILES:${PN}-dbg += "/boot/.debug"
