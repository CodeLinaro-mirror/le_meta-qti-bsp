DESCRIPTION = "HS-I2S driver"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://source.codeaurora.org/platform/vendor/qcom-opensource/hsi2s-kernel.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/hsi2s-kernel;nobranch=1"

SRCREV="3be812645c890e0490117ac1160f8f1887321947"

DEPENDS = "virtual/kernel"

S = "${WORKDIR}/vendor/qcom/opensource/hsi2s-kernel"

inherit module module-sign kernel-arch qperf
INHIBIT_PACKAGE_STRIP = "1"

EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"
