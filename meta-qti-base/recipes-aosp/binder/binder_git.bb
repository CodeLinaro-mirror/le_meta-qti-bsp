SUMMARY = "Android Binder IPC Support"
DESCRIPTION = "Intergrate binder daemon and configure proper binder device for android/android-like(starfish) system or module"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS += "glib-2.0 libcutils libhardware liblog libselinux system-core"

SRCREV = "${AUTOREV}"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks/binder;subpath=binder;usehead=1"
SRC_URI_append = " file://servicemanager.service"
SRC_URI_append = " file://create-binder.sh"

S = "${WORKDIR}/frameworks/binder"

inherit autotools pkgconfig systemd useradd

SYSTEMD_SERVICE_${PN} = "servicemanager.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

EXTRA_OECONF += "--with-glib"
# This recipe assumes kernel always compile for default arch even when
# multilib compilation is enabled. If kernel is 64bit and binder is compiled
# for 32bit due to multilib settings default 64bit IPC need to be supported
# as kernel is 64bit. Only when kernel is 32bit, 32bit IPC need to be enabled.
EXTRA_OECONF_append_arm = " \
    ${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32','','--enable-32bit-binder-ipc',d)} \
"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}/${sysconfdir}/initscripts/
        install -m 0755 ${WORKDIR}/create-binder.sh -D ${D}${sysconfdir}/initscripts/create-binder
        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/servicemanager.service -D ${D}${systemd_unitdir}/system/servicemanager.service
    fi
}

CFLAGS += "-I${STAGING_INCDIR}/libselinux"

QPERM_SERVICE = "${WORKDIR}/servicemanager.service"
