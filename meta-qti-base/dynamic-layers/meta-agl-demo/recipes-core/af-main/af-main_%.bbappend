FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
    file://afm-user-pre-setup.sh \
    file://user-pre-setup.conf \
"

do_install_append_class-target() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d -m 0755 ${D}${libexecdir}/afm
        install -m 0755 ${WORKDIR}/afm-user-pre-setup.sh ${D}${libexecdir}/afm/

        install -d -m 0755 ${D}${systemd_unitdir}/system/afm-user-setup@.service.d
        install -m 0644 ${WORKDIR}/user-pre-setup.conf ${D}${systemd_unitdir}/system/afm-user-setup@.service.d/pre-setup.conf
    fi
}

pkg_postinst_${PN} () {
    # Fail on error.
    set -e

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chgrp ${afm_name} $D${systemd_units_root}/system
        chgrp ${afm_name} $D${systemd_units_root}/system/afm-user-session@.target.wants
        chgrp ${afm_name} $D${systemd_units_root}/user/default.target.wants
        chgrp ${afm_name} $D${systemd_units_root}/user/sockets.target.wants
    fi
    chown ${afm_name}:${afm_name} $D${afm_datadir}
    chown ${afm_name}:${afm_name} $D${afm_datadir}/applications
    chown ${afm_name}:${afm_name} $D${afm_datadir}/icons

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/system
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/system/afm-user-session@.target.wants
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/user/default.target.wants
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/user/sockets.target.wants
    fi
    chsmack -a 'System::Shared' -t $D${afm_datadir}
    chsmack -a 'System::Shared' -t $D${afm_datadir}/applications
    chsmack -a 'System::Shared' -t $D${afm_datadir}/icons

    exit 0
}

pkg_postinst_ontarget_${PN} () {
}
