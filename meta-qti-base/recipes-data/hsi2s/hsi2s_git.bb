SUMMARY = "HS-I2S driver"
DESCRIPTION = "Recipe to generate the HS-I2S driver module. The driver configures the HS-I2S interfaces in the audio subsystem. It is typically used to receive high speed I2S data from radio tuners for Software Defined Radio(SDR) applications."
HOMEPAGE = "https://www.codeaurora.org/"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=434b8411d18d7f18ebe745bd3cc502ed"

DEPENDS += "virtual/kernel"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/hsi2s-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/hsi2s-kernel;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/hsi2s-kernel"

inherit module module-sign kernel-arch qperf
INHIBIT_PACKAGE_STRIP = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"
