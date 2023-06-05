do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'volatiled-var', 'true', 'false', d)}; then
        echo '#Change selinux store path to /persist because we change /var to tmpfs' >> ${D}/etc/selinux/semanage.conf
        echo 'store-root=/persist/lib/selinux' >> ${D}/etc/selinux/semanage.conf
    fi
}

