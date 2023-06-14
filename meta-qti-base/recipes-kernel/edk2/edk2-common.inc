PROVIDES = "virtual/bootloader"

DEPENDS += "util-linux-native"

TOOLCHAIN = "clang"

KERNEL_ARCH ?= "auto"

SRCREV = "${AUTOREV}"

inherit deploy

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
                'EARLY_ETH_ENABLED=${EARLY_ETH}'\
                'EARLY_ETH_AS_DLKM=1' \
                'UBSAN_UEFI_GCC_FLAG_ALIGNMENT=-Wno-misleading-indentation' \
                'TARGET_BOARD_TYPE_AUTO=1' \
                'SUPPORT_AB_BOOT_LXC=${AB_BOOT_LXC}' \
                ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'VERIFIED_BOOT_ENABLED=1', '', d)} \
                ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'VERIFIED_BOOT_2=1', '', d)} \
                'EXTRA_TARGET_OPTFLAGS=--sysroot=${STAGING_DIR_TARGET}'"

EXTRA_OEMAKE:append:sa81x5 = " 'AB_RETRYCOUNT_DISABLE=1' \
                               'BOOTIMAGE_LOAD_VERIFY_IN_PARALLEL=1' \
                               'ENABLE_LV_ATOMIC_AB=1' "

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
