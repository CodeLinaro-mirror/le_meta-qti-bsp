DESCRIPTION = "lib ptp"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=752b838e10ae75e6f917015849cf56b0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/ptp-virtual/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/ptp-virtual;usehead=1"
SRC_URI += " file://ptp-virtual.service"
SRCREV = "${AUTOREV}"

DEPENDS = "virtual/kernel"

S = "${WORKDIR}/vendor/qcom/opensource/ptp-virtual"

inherit module module-sign kernel-arch qperf
INHIBIT_PACKAGE_STRIP = "1"
EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}/etc
    install -m 0644 ${WORKDIR}/ptp-virtual.service ${D}${systemd_unitdir}/system/ptp-virtual.service
}

FILES_${PN} += "${systemd_unitdir}/system/ptp-virtual.service \
                /etc/* \
" 
