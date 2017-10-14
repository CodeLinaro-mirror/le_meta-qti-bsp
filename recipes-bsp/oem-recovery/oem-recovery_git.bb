inherit qcommon update-rc.d
PR = "r1"

DESCRIPTION = "OEM Recovery"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=device/qcom/common.git"

SRC_URI="${CAF_LA_GIT}/device/qcom/common.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=android_compat/device/qcom/common/recovery/oem-recovery;subpath=recovery/oem-recovery"

S = "${WORKDIR}/android_compat/device/qcom/common/recovery/oem-recovery/"

EXTRA_OECONF_append_class-native = " --with-core-headers=${STAGING_INCDIR_NATIVE}i/sparse"

EXTRA_OECONF_append_class-target = " --with-core-headers=${STAGING_INCDIR}/sparse"
EXTRA_OECONF_append_class-target += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include" 

PARALLEL_MAKE = ""
INITSCRIPT_NAME = "oem-recovery"
INITSCRIPT_PARAMS = "start 27 5 . stop 80 0 1 6 ."
