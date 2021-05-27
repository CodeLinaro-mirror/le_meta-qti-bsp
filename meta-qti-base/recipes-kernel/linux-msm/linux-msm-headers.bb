SUMMARY = "MSM Linux Kernel Headers"
DESCRIPTION = "Installs MSM kernel headers required to build userspace. \
These headers are installed in ${includedir}/linux-msm path."

SECTION = "kernel"

LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "unifdef-native bison-native rsync-native"

S = "${STAGING_KERNEL_DIR}"
B = "${WORKDIR}/build"

inherit linux-kernel-base kernel-arch

# We need the kernel to be unpacked and patched before we can grab the headers.
do_install[depends] += "virtual/kernel:do_patch"

# There's nothing to do here, except install the headers where we can package them
do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # Generate kernel headers
    rm -rf ${B}
    oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${B}
    install -d ${D}/usr/include
    mv ${B}/usr/include ${D}/usr/include/linux-msm

    # Need to create a hierarchy that works for Adreno's expectation that
    # its KERN_INCDIR variable is pointed at a directory with usr/include
    # below it with the headers.  Special casing for Adreno as opposed to
    # having to use ${STAGING_INCDIR}/X/usr/include everywhere seems like
    # a cleaner approach, and this could be removed if the Adreno makefiles
    # can be changed to just use KERN_INCDIR directly.
    # A separate hierarchy as opposed to one under ${D}/usr/include/linux-msm is
    # used to avoid issues from a symlink loop.
    install -d ${D}/usr/include/kernel/usr
    ln -sf ../../linux-msm ${D}/usr/include/kernel/usr/include
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
