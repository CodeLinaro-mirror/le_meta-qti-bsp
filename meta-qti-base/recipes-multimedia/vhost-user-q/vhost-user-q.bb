inherit systemd

DESCRIPTION = "vhost user qti binary"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PR = "r0"
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/vhost-user/.git;protocol=${PROTO};destsuffix=vhost-user-q;usehead=1"
SRCREV = "${AUTOREV}"
SYSROOT_DIRS += "${bindir}"

SRC_URI += "file://vhost-user-gpu.service"
S = "${WORKDIR}/vendor/qcom/opensource/vhost-user/"

DEPENDS = "virtual/kernel"
RDEPENDS_${PN} = ""

CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include -I${WORKDIR}/vendor/qcom/opensource/vhost-user/"
CFLAGS += " --sysroot=${STAGING_DIR_TARGET}"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
       ${CC} ${CFLAGS} -D__linux__ -o vhost-user-qti vhost_user_q.c vhost_ioctl.c
}

do_install() {
       install -d ${D}${bindir}
       install -m 0755 ${S}/vhost-user-qti ${D}${bindir}
       install -m 0644 ${WORKDIR}/vhost-user-gpu.service -D ${D}${systemd_unitdir}/system/vhost-user-gpu.service
}

PCKAGES = "${PN} ${PN}-dbg"
FILES_${PN} = "${bindir}/vhost-user-qti"
FILES_${PN} += "${systemd_unitdir}/system/*"
FILES_${PN}-dbg = "${bindir}/.debug/vhost-user-qti"
