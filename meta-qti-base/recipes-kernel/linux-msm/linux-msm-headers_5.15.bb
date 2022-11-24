SUMMARY = "MSM Linux Kernel Headers"
DESCRIPTION = "Installs MSM kernel headers required to build userspace. \
These headers are installed in ${includedir}/linux-msm path."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "bison-native rsync-native unifdef-native virtual/kernel"

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
    install -d ${D}${includedir}
    mv ${B}${includedir} ${D}${includedir}/linux-msm
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN}-dev = ""
