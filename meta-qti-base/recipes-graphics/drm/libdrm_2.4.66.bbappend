# 1. Update libdrm source code to QTI version
# 2. Add a patch to include <sys/sysmacros.h> for major() and minor() defination

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI = "git://source.codeaurora.org/platform/external/libdrm.git;protocol=${PROTO};destsuffix=graphics/libdrm;nobranch=1"
SRC_URI_append = " file://0001-include-sys-sysmacros.h-for-major-minor-definations.patch "

SRCREV = "b404411b9b1842297341c82117e59898501d016b"

S = "${WORKDIR}/graphics/libdrm"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', '--enable-drm_fe=yes', '', d)}"
