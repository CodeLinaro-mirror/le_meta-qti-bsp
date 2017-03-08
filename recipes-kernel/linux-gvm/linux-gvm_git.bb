# This file was derived from the linux-yocto-custom.bb recipe in
# oe-core.
#
# linux-yocto-msm8996.bb:
#
#   A yocto-bsp-generated kernel recipe that uses the linux-yocto and
#   oe-core kernel classes to apply a subset of yocto kernel
#   management to git managed kernel repositories.
#
# Warning:
#
#   Building this kernel without providing a defconfig or BSP
#   configuration will result in build or boot errors. This is not a
#   bug.
#
# Notes:
#
#   patches: patches can be merged into to the source git tree itself,
#            added via the SRC_URI, or controlled via a BSP
#            configuration.
#
#   example configuration addition:
#            SRC_URI += "file://smp.cfg"
#   example patch addition:
#            SRC_URI += "file://0001-linux-version-tweak.patch
#   example feature addition:
#            SRC_URI += "file://feature.scc"
#


inherit kernel
require recipes-kernel/linux/linux-yocto.inc
require recipes-kernel/linux/linux-msm.inc
require ${MACHINE}-image.inc

DEPENDS += "mkbootimg-native"
COMPATIBLE_MACHINE = "(8x96autogvmquin|8x96autogvmred)"

LINUX_VERSION ?= "3.18"
LINUX_VERSION_EXTENSION ?= "8996"

PV = "${LINUX_VERSION}"
PR = "r2"

KCONFIG_MODE="--alldefconfig"

do_install_append() {
	oe_runmake headers_install INSTALL_HDR_PATH=${STAGING_KERNEL_BUILDDIR}/usr ARCH=$ARCH
}

do_deploy_append() {
   rm -f  "${DEPLOYDIR}/boot.img" "{DEPLOYDIR}/initrd"
   touch "${DEPLOYDIR}/initrd"
   mkbootimg --kernel "${DEPLOYDIR}/${KERNEL_IMAGETYPE}" --ramdisk "${DEPLOYDIR}/initrd"  -o "${DEPLOYDIR}/boot.img" --cmdline "${KERNEL_CMDLINE}" --base "${KERNEL_BASE}"
}

do_removegit () {
   rm -rf "${S}/.git"
   rm -rf "${S}/.meta"
   rm -rf "${S}/.metadir"
}

addtask do_removegit after do_unpack before do_kernel_checkout
