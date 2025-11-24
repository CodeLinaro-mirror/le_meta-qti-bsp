DEPENDS += "util-linux-native"

PROVIDES = "virtual/bootloader"

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
                'VERIFIED_BOOT_LE=0'\
                'VERITY_LE=0'\
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

EXTRA_OEMAKE:append:monaco = " 'AB_RETRYCOUNT_DISABLE=1' \
                               'ENABLE_LV_ATOMIC_AB=1' "
EXTRA_OEMAKE:append:gvm-gen5 = " 'AUTO_VIRT_ABL=1' \
                               'DISABLE_KERNEL_PROTOCOL=1' \
                               'AUTO_LVGVM_ABL=1' "

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

do_compile:gvm-gen5 () {
    install -d ${D}/boot
    # generate fake elf files to compile.
    python3 -c '
import struct
import os
import sys

def create_elf(output_file, size_kb):
    size_bytes = size_kb * 1024
    elf_header = bytearray([
        0x7f, 0x45, 0x4c, 0x46,
        0x02, 0x01, 0x01, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x02, 0x00, 0xb7, 0x00,
        0x01, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x40, 0x00, 0x38, 0x00,
        0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    ])
    program_header = bytearray([
        0x01, 0x00, 0x00, 0x00,
        0x05, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    ])
    program_header.extend(struct.pack("<Q", size_bytes))
    program_header.extend(struct.pack("<Q", size_bytes))
    program_header.extend(struct.pack("<Q", 0x1000))
    code = bytearray([0x00, 0x00, 0x80, 0xd2, 0xc0, 0x03, 0x5f, 0xd6])
    padding_size = size_bytes - len(elf_header) - len(program_header) - len(code)
    padding = bytearray(padding_size)

    with open(output_file, "wb") as f:
        f.write(elf_header)
        f.write(program_header)
        f.write(code)
        f.write(padding)

create_elf("${D}/boot/${PRODUCT}-abl.elf", 244)
create_elf("${D}/boot/abl.elf", 242)
create_elf("${D}/boot/unsigned_abl.elf", 242)
create_elf("${D}/boot/LinuxLoader.efi", 634)
'

    install -m 0644 ${D}/boot/${PRODUCT}-abl.elf ${S}/../${PRODUCT}-abl.elf
    install -m 0644 ${D}/boot/abl.elf ${S}/../abl.elf
    install -m 0644 ${D}/boot/unsigned_abl.elf ${S}/../unsigned_abl.elf
    install -m 0644 ${D}/boot/LinuxLoader.efi ${S}/../LinuxLoader.efi
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

do_deploy:append:gvm-gen5() {
    install -m 0644 ${S}/../LinuxLoader.efi ${DEPLOYDIR}
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
