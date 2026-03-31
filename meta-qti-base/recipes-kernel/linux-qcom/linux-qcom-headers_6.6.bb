SUMMARY = "QCOM Linux Kernel Headers"
DESCRIPTION = "Installs QCOM kernel headers required to build userspace. \
These headers are installed in ${includedir}/linux-qcom path."
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "kernel"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "bison-native rsync-native unifdef-native"

PROVIDES += "virtual/kernel-headers"

S = "${STAGING_KERNEL_DIR}"
B = "${WORKDIR}/build"

inherit linux-kernel-base kernel-arch

# We need the kernel to be unpacked and patched before we can grab the headers.
# Also need soc-repo headers to be overlaid before extracting
do_install[depends] += "virtual/kernel:do_patch soc-repo:do_configure"

# There's nothing to do here, except install the headers where we can package them
do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_populate_lic[noexec] = "1"
do_install() {
    # Generate kernel headers
    rm -rf ${B}
    oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${B}
    install -d ${D}${includedir}
    mv ${B}${includedir} ${D}${includedir}/linux-qcom

    # Need to create a hierarchy that works for Adreno's expectation that
    # its KERN_INCDIR variable is pointed at a directory with usr/include
    # below it with the headers.  Special casing for Adreno as opposed to
    # having to use ${STAGING_INCDIR}/X/usr/include everywhere seems like
    # a cleaner approach, and this could be removed if the Adreno makefiles
    # can be changed to just use KERN_INCDIR directly.
    # A separate hierarchy as opposed to one under ${D}/usr/include/linux-qcom is
    # used to avoid issues from a symlink loop.
    install -d ${D}${includedir}/kernel/usr
    ln -sf ../../linux-qcom ${D}${includedir}/kernel/usr/include

    # Create a symlink for linux-qcom-rt -> linux-qcom so that recipes using
    # ${PREFERRED_PROVIDER_virtual/kernel} (i.e. linux-qcom-rt) to construct
    # their include path can transparently find the headers.
    ln -sf linux-qcom ${D}${includedir}/linux-qcom-rt
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
ALLOW_EMPTY:${PN} = "1"

RPROVIDES:${PN}-dev += "linux-qcom-rt-headers-dev"
