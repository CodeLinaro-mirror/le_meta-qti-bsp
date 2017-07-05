
do_install_append_smack () {
     # replace the old smack rules from "System _ -----l" to "System _ rwxa--"
     sed -i "1c System _ rwxa--"  ${D}/${sysconfdir}/smack/accesses.d/default-access-domains
}
