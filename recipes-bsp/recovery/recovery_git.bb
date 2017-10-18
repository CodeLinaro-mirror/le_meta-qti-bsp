inherit update-rc.d qcommon
PR = "r7"

DESCRIPTION = "Recovery bootloader"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=platform/bootable/recovery.git"

DEPENDS += "libmincrypt-native system-core oem-recovery ext4-utils"
RDEPENDS_${PN} = "zlib bzip2"

# minadbd need adb headers from system-core project to compile.
SRC_URI = " \
    ${CAF_LA_GIT}/platform/bootable/recovery.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=bootable/recovery \
    ${CAF_LA_GIT}/platform/system/core.git;nobranch=1;protocol=git;tag=${CAF_TAG};destsuffix=system/core/adb;subpath=adb \
"

SRC_URI += "file://recovery.service"
SRC_URI += "file://fstab"

S = "${WORKDIR}/bootable/${PN}/"

EXTRA_OECONF = "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                --with-core-headers=${STAGING_INCDIR}"

CPPFLAGS += "-I${STAGING_INCDIR}/fs_mgr"
CPPFLAGS += "-I${STAGING_INCDIR}/mincrypt"
CPPFLAGS += "-I${STAGING_INCDIR}/ext4_utils"
CPPFLAGS += "-I${WORKDIR}/system/core/adb"

SYSTEMD_SUPPORT = "${@base_contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

PARALLEL_MAKE = ""
INITSCRIPT_NAME = "recovery"
INITSCRIPT_PARAMS = "start 27 5 . stop 80 0 1 6 ."

FILES_${PN} += "/cache"
FILES_${PN} += "/system"
FILES_${PN} += "/tmp"
FILES_${PN} += "/res"
FILES_${PN} += "/data"
FILES_${PN} += "/lib"
do_install_append() {
        install -d ${D}/cache/
        install -d ${D}/tmp/
        install -d ${D}/res/
        install -d ${D}/data/
        install -d ${D}/system/
        install -m 0755 ${S}/start_recovery -D ${D}${sysconfdir}/init.d/recovery

        if [ "${SYSTEMD_SUPPORT}" == "systemd" ]; then
              install -m  0755 ${WORKDIR}/fstab -D ${D}${sysconfdir}/fstab
              install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab -D ${D}/res/recovery_volume_config
              install -d ${D}${systemd_unitdir}/system/
              install -m 0644 ${WORKDIR}/recovery.service -D ${D}${systemd_unitdir}/system/recovery.service
              install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
              # enable the service for multi-user.target
              ln -sf ${systemd_unitdir}/system/recovery.service \
                            ${D}${systemd_unitdir}/system/multi-user.target.wants/recovery.service
        else
              install -m  0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab -D ${D}${sysconfdir}/fstab
              install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab -D ${D}/res/recovery_volume_config
        fi
}
