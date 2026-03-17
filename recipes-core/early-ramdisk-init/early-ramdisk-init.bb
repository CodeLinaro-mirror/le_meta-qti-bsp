SUMMARY = "early-ramdisk-init for load kernel modules and start rootfs init"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"
DEPENDS = "kmod util-linux"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI = "file://vendor/qcom/opensource/early-ramdisk-init"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/early-ramdisk-init"

inherit autotools

EXTRA_OECONF += "--bindir=${base_sbindir} --sbindir=${base_sbindir}"

CFLAGS += '-DLOG_DIR=\\"/boot/early-ramdisk\\"'
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'early_init', '-DEARLY_INIT', '', d)}"

CFLAGS:append:quin-gvm-gen4-5 = " -DLIB_UNIFICATION"
CFLAGS:append:gvm-gen4-5 = " -DLIB_UNIFICATION"
CFLAGS:append:canoe = " -DENABLE_LE_VARIANT -DFIRMWARE_MOUNT"

TARGET_PATH_NAME ?= "${MACHINE}"

do_install:append() {
    install -d ${D}/dev
    install -d ${D}/sys
    install -d ${D}/proc
    install -d ${D}/etc
    install -d ${D}/boot/early-ramdisk
    install -d ${D}/realroot
    install -d ${D}/etc/modules-load.f
    install -d ${D}/etc/modules-load.l
    touch ${D}/init
    install -m 0755 ${S}/conf/${TARGET_PATH_NAME}/*.conf -D ${D}/etc/modules-load.f/
    install -m 0755 ${S}/conf/${TARGET_PATH_NAME}/*.late -D ${D}/etc/modules-load.l/
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-external-boot', 'true', 'false', d)}; then
        install -m 0755 ${S}/conf/${TARGET_PATH_NAME}/02-external-bootup.conf.in -D ${D}/etc/modules-load.f/02-external-bootup.conf
    fi
}

FILES:${PN} += "\
         init \
         sys/ \
         dev/ \
         etc/ \
         proc/ \
         boot/early-ramdisk \
         realroot/ \
         etc/modules-load.f/* \
"
