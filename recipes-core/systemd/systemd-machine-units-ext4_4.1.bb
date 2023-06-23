DESCRIPTION = "Systemd machine units for ext4 image"
include qti-systemd-machine-units.inc

IMAGETYPE = "ext4"

fix_sepolicies_ext4 () {
    sed -i "s#,rootcontext=system_u:object_r:cache_t:s0##g" ${WORKDIR}/cache.mount
}

do_install[prefuncs] += " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '', 'fix_sepolicies', d)}"
do_install[prefuncs] += " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-ab-boot', 'fix_mount_services', '', d)}"
do_install[prefuncs] += " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '', 'fix_sepolicies_ext4', d)}"

do_install_append () {

    if ${@bb.utils.contains('MACHINE_MNT_POINTS', '$userfsdatadir', 'true', 'false', d)}; then
        # Run fsck at boot
        install -d 0644 ${D}${systemd_unitdir}/system/local-fs-pre.target.requires
        ln -sf ${systemd_unitdir}/system/systemd-fsck@.service \
            ${D}${systemd_unitdir}/system/local-fs-pre.target.requires/systemd-fsck@dev-disk-by\\x2dpartlabel-userdata.service
    fi

    if ${@bb.utils.contains('COMBINED_FEATURES', 'qti-ab-boot', 'true', 'false', d)}; then
        install -m 0644 ${S}/set-slotsuffix.service ${D}${systemd_unitdir}/system
    fi

    if ${@bb.utils.contains('MACHINE_MNT_POINTS', '/overlay', 'true', 'false', d)}; then
        install -m 0644 ${S}/machine-id-mount.service ${D}${systemd_unitdir}/system
        ln -s ${systemd_unitdir}/system/machine-id-mount.service ${D}${systemd_unitdir}/system/local-fs.target.wants/machine-id-mount.service
    fi

}

SYSTEMD_SERVICE_${PN} += "${@bb.utils.contains('COMBINED_FEATURES','qti-ab-boot',' set-slotsuffix.service','',d)}"
