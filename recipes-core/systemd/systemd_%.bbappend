#Add qti specific changes only when qt-disro is enabled.
QTI_SYSTEMD_INC = ""
QTI_SYSTEMD_INC_qti-distro-base = "${THISDIR}/qti-systemd.inc"
include ${QTI_SYSTEMD_INC}

do_install_append() {
    if ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-vm', 'true', 'false', d)}; then
        sed -i 's/#children_max=/children_max=5/' ${D}/etc/udev/udev.conf
    fi
    sed -i '/group:wheel/d' ${D}${exec_prefix}/lib/tmpfiles.d/systemd.conf
}
