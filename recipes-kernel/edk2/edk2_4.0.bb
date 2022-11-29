inherit deploy nopackages
DESCRIPTION = "UEFI bootloader"
LICENSE = "BSD-2-Clause-Patent"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/BSD-2-Clause-Patent;md5=0518d409dae93098cca8dfa932f3ab1b \
"

FILESEXTRAPATHS:prepend := "${KERNEL_PREBUILT_DISTDIR}/:"

SRC_URI = "file://abl.elf"

do_install[noexec]="1"
do_configure[noexec]="1"
do_compile[noexec]="1"

do_deploy() {
    install -m 644 ${WORKDIR}/abl.elf ${DEPLOYDIR}
}

do_deploy[dirs] = "${DEPLOYDIR}"
addtask deploy before do_build after do_install

