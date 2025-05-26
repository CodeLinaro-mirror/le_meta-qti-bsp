do_install:append:sa8775() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        echo '#Change selinux store path to /data from /var because no enough space to store policies' >> ${D}${sysconfdir}/selinux/semanage.conf
        echo 'store-root=/data/lib/selinux' >> ${D}${sysconfdir}/selinux/semanage.conf
    fi
}

do_install:append:sa7255() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        echo '#Change selinux store path to /data from /var because no enough space to store policies' >> ${D}${sysconfdir}/selinux/semanage.conf
        echo 'store-root=/data/lib/selinux' >> ${D}${sysconfdir}/selinux/semanage.conf
    fi
}

do_install:append:quin-gvm-lemans() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES', 'volatiled-var', 'true', 'false', d)}; then
            echo '#Change selinux store path to /persist because we change /var to tmpfs' >> ${D}${sysconfdir}/selinux/semanage.conf
            echo 'store-root=/persist/lib/selinux' >> ${D}${sysconfdir}/selinux/semanage.conf
        fi
    fi
}
