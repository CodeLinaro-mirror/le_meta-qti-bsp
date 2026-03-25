SUMMARY = "vmm boot lifecycle manager binary"
DESCRIPTION = "Manage the boot lifecycle of vms through vmm service"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "glib-2.0 vmm-lib abctl"

WORKSPACE_DIR := "${@os.path.dirname(d.getVar('TOPDIR'))}"
FILESEXTRAPATHS:prepend := "${WORKSPACE_DIR}:"
SRC_URI = "file://vendor/qcom/opensource/vmm-boot-lcm"

S = "${WORKDIR}/vendor/qcom/opensource/vmm-boot-lcm"
RDEPENDS:${PN} = "vmm-lib abctl"

SYSTEMD_SERVICE:${PN} = "vmm-boot-lcm.service"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit cmake pkgconfig systemd

do_install:append() {
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${S}/vmm-boot-lcm.service ${D}/${systemd_unitdir}/system/vmm-boot-lcm.service
}
