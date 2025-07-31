SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

DEPENDS += "mkbootimg-native"

inherit core-image

require automotive-image.inc

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

add_extra_modules() {
    # add modules under /extra and remove qcaxxxx.ko and sat_module.ko)
    cd ${IMAGE_ROOTFS}/lib/modules/${KERNEL_VERSION}
    find extra -type f -name "*.ko" | grep -v qca |grep -v sat_module| sort >> modules.order

    # genarate modules.load from modules.order
    awk -F / '{print $NF > "modules.load"}' modules.order
}

ROOTFS_POSTPROCESS_COMMAND:append = " add_extra_modules;"
ROOTFS_POSTPROCESS_COMMAND:remove:sa8797 = "add_extra_modules;"

# Makes image suitable for development (e.g. enable ssh for login, allows root logins and logins without passwords by ssh)
IMAGE_FEATURES:append = " ${@bb.utils.contains('VARIANT', 'debug', 'debug-tweaks ssh-server-openssh', '', d)}"

INCOMPATIBLE_LICENSE = "GPL-3.0* LGPL-3.0* AGPL-3.0*"
INCOMPATIBLE_LICENSE_EXCEPTIONS:quin-gvm-gen4 = "bash:GPL-3.0-or-later"

INCOMPATIBLE_LICENSE_EXCEPTIONS:quin-gvm-lemans = "\
    gdbserver:GPL-3.0-only \
    gdbserver:LGPL-3.0-only \
    gdb:GPL-3.0-only \
    gdb:LGPL-3.0-only \
    binutils:GPL-3.0-only \
    python3-docutils:GPL-3.0-only \
    bison:GPL-3.0-only \
    gawk:GPL-3.0-only \
    tar:GPL-3.0-only \
    m4:GPL-3.0-only \
    "

INCOMPATIBLE_LICENSE_EXCEPTIONS:quin-gvm-gen4-5 = "\
    gdbserver:GPL-3.0-only \
    gdbserver:LGPL-3.0-only \
    gdb:GPL-3.0-only \
    gdb:LGPL-3.0-only \
    binutils:GPL-3.0-only \
    python3-docutils:GPL-3.0-only \
    bison:GPL-3.0-only \
    gawk:GPL-3.0-only \
    tar:GPL-3.0-only \
    m4:GPL-3.0-only \
    "
