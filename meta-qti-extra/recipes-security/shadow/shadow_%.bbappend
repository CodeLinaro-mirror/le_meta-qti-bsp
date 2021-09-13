do_install_append_class-target() {
    # Comment out the validation to selinux user as workaround to login LV host with console at selinux enforcing
    # mode because currently no mapping between linux user and selinux user.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        sed -i '/pam_selinux/s/^/#/g' ${D}${sysconfdir}/pam.d/login
    fi
}
