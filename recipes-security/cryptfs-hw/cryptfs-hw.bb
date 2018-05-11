inherit autotools pkgconfig

DESCRIPTION = "Build Filesystem Encryption Decryption Enabler-library utilising underlying crypto-hardware"
HOMEPAGE = ""

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=3775480a712fc46a69647678acb234cb"
PR = "r0"

FILESPATH =+ "${WORKSPACE}/android_compat/device/qcom/common/:"
SRC_URI = "file://cryptfs_hw"

S = "${WORKDIR}/cryptfs_hw"

DEPENDS += "libcutils libhardware system-core libselinux libscrypt"

EXTRA_OECONF += "--with-emmc-use-ICE"
