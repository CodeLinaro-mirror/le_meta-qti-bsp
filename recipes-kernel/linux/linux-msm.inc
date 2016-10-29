DESCRIPTION = "Linux kernel"
SECTION     = "kernel"
LICENSE     = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:${THISDIR}/linux-msm:"
SRC_URI   =  "file://kernel"
SRC_URI += " \
	file://0001-ARM-dts-msm-Add-support-for-msm8996-auto-mctm.patch \
	file://0002-defconfig-msm-Enable-CONFIG_MSM8996_MCTM-for-MCTM.patch \
"


S =  "${WORKDIR}/kernel"
