do_install:append() {
    # Skip pam_selinux check to login as workaround the issue that User Login service can't start up
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        sed -i '/pam_selinux/s/^/#/g' ${D}${sysconfdir}/pam.d/login
    fi
}
