
do_install_append_smack () {
     # replace the old smack rules from "System _ -----l" to "System _ rwxa--"
     sed -i "1c System _ rwxa--"  ${D}/${sysconfdir}/smack/accesses.d/default-access-domains
}

do_install_append () {
    # Early_init will mount root as rw
    if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)}; then
        sed -i '/\/dev\/root/d' ${D}/${sysconfdir}/fstab
    fi
}
