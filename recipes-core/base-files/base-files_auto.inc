
do_install_append_smack () {
     # replace the old smack rules from "System _ -----l" to "System _ rwxa--"
     sed -i "1c System _ rwxa--"  ${D}/${sysconfdir}/smack/accesses.d/default-access-domains
}

do_install_append () {
    if ${@bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', 'true', 'false', d)}; then
        ln -sf ../usr/share/zoneinfo/UTC ${D}/etc/localtime
        ln -sf ../proc/self/mounts       ${D}/etc/mtab
    fi
}
