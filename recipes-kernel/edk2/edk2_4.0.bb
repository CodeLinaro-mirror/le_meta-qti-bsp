inherit deploy nopackages
DESCRIPTION = "UEFI bootloader"
LICENSE = "BSD-2-Clause-Patent"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/BSD-2-Clause-Patent;md5=0518d409dae93098cca8dfa932f3ab1b \
"

INHIBIT_DEFAULT_DEPS = "1"
FILESEXTRAPATHS:prepend := "${KERNEL_PREBUILT_PATH}/dist:"

SRC_URI = "file://abl_userdebug.elf"

do_install[noexec]="1"
do_configure[noexec]="1"

do_deploy() {
    install -m 644 ${WORKDIR}/abl_userdebug.elf ${DEPLOYDIR}/abl.elf
}

do_deploy[dirs] = "${DEPLOYDIR}"
addtask deploy before do_build after do_install

