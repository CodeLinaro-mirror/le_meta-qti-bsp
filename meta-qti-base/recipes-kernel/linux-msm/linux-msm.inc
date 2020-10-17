DESCRIPTION = "CAF Linux Kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
DEPENDS += " mkbootimg-native openssl-native kern-tools-native"
DEPENDS_append_aarch64 = " libgcc"
SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "kernel_data"

SRC_URI = "${PATH_TO_REPO}/kernel/msm-4.14/.git;protocol=${PROTO};destsuffix=kernel/msm-4.14;usehead=1 \
           ${PATH_TO_REPO}/data-kernel/.git;protocol=${PROTO};destsuffix=data-kernel;usehead=1 \
           file://cgroup.cfg \
           file://ipc.cfg \
           file://memhotplug.cfg \
           file://lxc.cfg \
           ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' file://systemd.cfg', '', d)} \
           ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', ' file://virtualization.cfg', '', d)} \
           ${@bb.utils.contains('DISTRO_FEATURES', 'nand-squashfs', ' file://squashfs.cfg', '', d)} \
           ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', ' file://earlyuserspace.cfg', '', d)} \
           ${@bb.utils.contains('DISTRO_FEATURES', 'qti-dual-wlan', ' file://dual-wlan.cfg', '', d)} \
           ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', ' file://weston.cfg', '', d)} \
           ${@bb.utils.contains('DISTRO_FEATURES', 'ktf', ' file://ktf.cfg', '', d)} \
           "

inherit kernel kernel-yocto

do_compile () {
    oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
}

PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""

# extra task for configuration checks
addtask kernel_configcheck after do_configure before do_compile

python __anonymous () {
  # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
  # to kernel.bbclass. We override the variable as msm kernel can't
  # support alternate image builds
  if d.getVar("KERNEL_IMAGETYPE", True):
      d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "")
}

KERNEL_PRIORITY = "9001"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"

KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS += "O=${B}"
KERNEL_EXTRA_ARGS_append_sa8155 += "TARGET_BOARD_TYPE=auto"
KERNEL_EXTRA_ARGS_append_sa6155 += "TARGET_BOARD_TYPE=auto"
KERNEL_EXTRA_ARGS_append_sa8195 += "TARGET_BOARD_TYPE=auto"
KBRANCH ?= ""
KMETA = "kernel-meta"
KMACHINE ?= "${BASEMACHINE}"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG ?= "${KERNEL_CONFIG}"
LINUX_VERSION_EXTENSION = "${@['-perf', ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"
do_kernel_checkout[noexec] = "1"
do_deploy() {
    if [[ ${KERNEL_IMAGETYPE} != *-dtb ]]; then
        bberror "${PN}: Only appended DTB supported; Change KERNEL_IMAGETYPE to ${KERNEL_IMAGETYPE}-dtb in your kernel config."
        return
    fi
    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi
}
