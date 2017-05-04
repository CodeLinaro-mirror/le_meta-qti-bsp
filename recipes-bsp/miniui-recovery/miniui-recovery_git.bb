inherit autotools-brokensep pkgconfig update-rc.d
PR = "r1"

DESCRIPTION = "Mini UI Recovery"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=device/qcom/common.git"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://android_compat/device/qcom/common/recovery/miniui/"

S = "${WORKDIR}/android_compat/device/qcom/common/recovery/miniui/"

EXTRA_OECONF = "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                --with-core-headers=${STAGING_INCDIR_NATIVE}"

PARALLEL_MAKE = ""
INITSCRIPT_NAME = "mini-ui-recovery"
INITSCRIPT_PARAMS = "start 27 5 . stop 80 0 1 6 ."
