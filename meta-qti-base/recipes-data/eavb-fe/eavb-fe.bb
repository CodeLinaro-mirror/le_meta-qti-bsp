DESCRIPTION = "Build an external Linux kernel module for eAVB (Ethernet Audio Video Bridging) Front-End Driver"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/ptp-virtual/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/ptp-virtual/eavb_fe;subpath=eavb_fe;usehead=1 \
           file://eavb_load.conf"

SRCREV = "${AUTOREV}"

DEPENDS = "virtual/kernel"

S = "${WORKDIR}/vendor/qcom/opensource/ptp-virtual/eavb_fe"

inherit module module-sign kernel-arch

EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

do_install_append() {
    install -m 0755 ${WORKDIR}/eavb_load.conf -D ${D}${sysconfdir}/modules-load.d/eavb_load.conf
}

FILES_${PN} += "${sysconfdir}/*"
