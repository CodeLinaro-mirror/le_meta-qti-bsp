SUMMARY = "CLO Linux Kernel"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

require recipes-kernel/linux-msm/linux-msm.inc

COMPATIBLE_MACHINE = "monaco"

SRC_URI = "${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/msm-kernel/.git;protocol=${PROTO};destsuffix=kernel/kernel-${PV}/kernel_platform/msm-kernel;usehead=1"

S = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/msm-kernel"
