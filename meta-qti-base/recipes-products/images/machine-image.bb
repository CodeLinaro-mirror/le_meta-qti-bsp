require automotive-image.inc
SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

DEPENDS += "mkbootimg-native"

inherit core-image

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

add_extra_modules() {
    # add modules under /extra and remove qcaxxxx.ko and sat_module.ko)
    cd ${IMAGE_ROOTFS}/lib/modules/${KERNEL_VERSION}
    find extra -type f -name "*.ko" | grep -v qca |grep -v sat_module| sort >> modules.order

    # genarate modules.load from modules.order
    awk -F / '{print $NF > "modules.load"}' modules.order
}

ROOTFS_POSTPROCESS_COMMAND:append = " ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "add_extra_modules", "", d)};"
