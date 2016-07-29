inherit deploy

DESCRIPTION = "Little Kernel bootloader"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=0835ade698e0bcf8506ecda2f7b4f302"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=kernel/lk.git"

PROVIDES = "virtual/bootloader"
FILESPATH =+ "${WORKSPACE}:"
SRC_URI  = "file://lk/"
S        = "${WORKDIR}/lk"
PR       = "r15"

MY_TARGET       = "msm8996"

PACKAGE_ARCH    = "${MACHINE_ARCH}"
BASEMACHINE        = "${@d.getVar('MACHINE', True)}"

LIBGCC          = "${STAGING_LIBDIR}/${TARGET_SYS}/5.2.0/libgcc.a"

EXTRA_OEMAKE = "${MY_TARGET} TOOLCHAIN_PREFIX='${TARGET_PREFIX}' LIBGCC='${LIBGCC}' EMMC_BOOT=1 SIGNED_KERNEL=1 VERIFIED_BOOT=1 DEFAULT_UNLOCK=true"

do_install() {
         install -d ${D}/boot
         install build-${MY_TARGET}/*.mbn ${D}/boot
}
FILES_${PN} = "/boot"
FILES_${PN}-dbg = "/boot/.debug"

do_deploy() {
        install ${S}/build-${MY_TARGET}/*.mbn ${DEPLOYDIR}
}

do_deploy[dirs] = "${S} ${DEPLOYDIR}"
addtask deploy before do_build after do_install

PACKAGE_STRIP = "no"
