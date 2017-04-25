inherit autotools-brokensep pkgconfig update-rc.d
PR = "r1"

DESCRIPTION = "OEM Recovery"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=device/qcom/common.git"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://android_compat/device/qcom/common/recovery/oem-recovery/"

S = "${WORKDIR}/android_compat/device/qcom/common/recovery/oem-recovery/"

EXTRA_OECONF = "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                --with-core-headers=${STAGING_INCDIR_NATIVE}"

PARALLEL_MAKE = ""
INITSCRIPT_NAME = "oem-recovery"
INITSCRIPT_PARAMS = "start 27 5 . stop 80 0 1 6 ."
