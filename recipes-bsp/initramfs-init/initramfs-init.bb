DESCRIPTION = "Initialize scripts for initramfs"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=48b43ba58d0f8e9ef3704313a46b7a43"

RDEPENDS_${PN} += " busybox"
SRC_URI = "file://initramfs_init.sh"
SRC_URI += "file://keyfile"
PR = "r0"

do_install() {
    install -m 0755 ${WORKDIR}/initramfs_init.sh ${D}/init

    install -d "${D}${sysconfdir}/keys"
    install -m 0755 ${WORKDIR}/keyfile "${D}${sysconfdir}/keys/keyfile"
}

FILES_${PN} += " /init"
FILES_${PN} += " /etc/keys/keyfile"
PACKAGE_ARCH = "${MACHINE_ARCH}"
