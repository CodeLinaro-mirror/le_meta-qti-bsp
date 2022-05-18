SUMMARY = "Kernel Test Framework Unit test for virtio spmi pmic driver"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "ktf"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/unit-test/kernel-unit-test/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/unit-test/kernel-unit-test/virtio_spmi;subpath=virtio_spmi;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/unit-test/kernel-unit-test/virtio_spmi"

inherit module module-sign

MODULES_PATH = "${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/unit_test"

python __anonymous () {
    d.setVar('KBUILD_EXTRA_SYMBOLS', "${STAGING_INCDIR}/ktf/Module.symvers")
}

do_install () {
    install -d ${D}/${base_libdir}/modules/${KERNEL_VERSION}/unit_test
    install -D -m 0644 ${S}/virtio_spmi_rw_unittest.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}/unit_test/
    install -D -m 0644 ${S}/virtio_spmi_irq_unittest.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}/unit_test/
}

FILES:${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/virtio_spmi_rw_unittest.ko"
FILES:${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/virtio_spmi_irq_unittest.ko"
