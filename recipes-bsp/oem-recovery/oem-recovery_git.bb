inherit qcommon update-rc.d
PR = "r1"

DESCRIPTION = "OEM Recovery"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=device/qcom/common.git"

# "edify/expr.h" from bootable/recovery is required for oem-recovery to compile.
SRC_URI = " \
    ${CAF_LA_GIT}/device/qcom/common.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=android_compat/device/qcom/common/recovery/oem-recovery;subpath=recovery/oem-recovery  \
    ${CAF_LA_GIT}/platform/bootable/recovery.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=bootable/recovery/edify;subpath=edify \
"

S = "${WORKDIR}/android_compat/device/qcom/common/recovery/oem-recovery/"

CPPFLAGS += "-I${WORKDIR}/bootable/recovery"

EXTRA_OECONF_append_class-native = " --with-core-headers=${STAGING_INCDIR_NATIVE}/sparse"

EXTRA_OECONF_append_class-target = " --with-core-headers=${STAGING_INCDIR}/sparse"
EXTRA_OECONF_append_class-target += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include" 

PARALLEL_MAKE = ""
INITSCRIPT_NAME = "oem-recovery"
INITSCRIPT_PARAMS = "start 27 5 . stop 80 0 1 6 ."
