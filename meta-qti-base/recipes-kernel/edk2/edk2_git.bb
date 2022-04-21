SUMMARY = "edk2"
DESCRIPTION = "UEFI bootloader"
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PROVIDES = "virtual/bootloader"

PR = "r1"
PV = "3.0"

SRC_URI = "${PATH_TO_REPO}/bootable/bootloader/edk2/.git;protocol=${PROTO};destsuffix=bootable/bootloader/edk2;usehead=1"
# FIXME for keymaster functionality, disable it for sa8295 target temporarily.
SRC_URI:append = " ${@oe.utils.conditional('BASEMACHINE','sa8295', '', 'file://0001-avb-bring-up-keymaster-for-LV.patch \
                                                                           file://0002-avb-send-dummy-ROT-and-boot-state-to-keymaster-from-.patch \
                   ', d)}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/bootable/bootloader/edk2"

inherit deploy

VBLE = "${@bb.utils.contains('DISTRO_FEATURES', 'vble','1', '0', d)}"
VERITY_ENABLED = "${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity','1', '0', d)}"
EARLY_ETH = "${@bb.utils.contains('DISTRO_FEATURES', 'early-eth', '1', '0', d)}"
HIBERNATION = "${@bb.utils.contains('COMBINED_FEATURES', 'hibernation', '1', '0', d)}"
DISABLE_NONBOOTDEVICE_ENABLED ?= "0"
DISABLE_NONBOOTDEVICE_ENABLED:sa6155 = "1"

EXTRA_OEMAKE = "'CLANG_BIN=${CLANG_BIN_PATH}' \
                'CLANG_PREFIX=${STAGING_BINDIR_NATIVE}/${TARGET_SYS}/${TARGET_PREFIX}' \
                'TARGET_ARCHITECTURE=${TARGET_ARCH}'\
                'BUILDDIR=${S}'\
                'BOOTLOADER_OUT=${S}/out'\
                'ENABLE_LE_VARIANT=true'\
                'HIBERNATION_SUPPORT=${HIBERNATION}'\
                'VERIFIED_BOOT_LE=${VBLE}'\
                'VERITY_LE=${VERITY_ENABLED}'\
                'INIT_BIN_LE=\"/sbin/init\"'\
                'EDK_TOOLS_PATH=${S}/BaseTools'\
                'EARLY_ETH_ENABLED=${EARLY_ETH}'\
                'UBSAN_UEFI_GCC_FLAG_ALIGNMENT=-Wno-misleading-indentation' \
                'SUPPORT_DISABLE_NON_BOOTDEVICE=${DISABLE_NONBOOTDEVICE_ENABLED}' \
                'TARGET_BOARD_TYPE_AUTO=1' \
                ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'VERIFIED_BOOT_2=1', '', d)} "

do_configure[noexec] = "1"
do_compile () {
    export CC=${BUILD_CC}
    export CXX=${BUILD_CXX}
    export LD=${BUILD_LD}
    export AR=${BUILD_AR}
    if ${@bb.utils.contains('MACHINE_FEATURES', 'goldcore-boot', 'true', 'false', d)}; then
        export LINUX_BOOT_CPU_SELECTION_ENABLED=1
        export TARGET_LINUX_BOOT_CPU_ID=7
    fi
    oe_runmake -f makefile all
}
do_install() {
    install -d ${D}/boot
}

do_deploy() {
    install -m 0644 ${D}/boot/abl.elf ${DEPLOYDIR}
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
