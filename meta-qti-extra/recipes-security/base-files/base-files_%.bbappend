# Fix selinux label from fstab for android container
# Remove contexts for /run and /var/volatile when refpolicy-qti is applied
fix_selinux_labels () {
    #For /firmware
    sed -i "s#^PARTLABEL=modem.*#PARTLABEL=modem \/firmware auto defaults,ro,slotselect,context=u:object_r:firmware_file:s0 0 0#g" ${WORKDIR}/fstab
    #For /bluetooth
    sed -i "s#^PARTLABEL=bluetooth.*#PARTLABEL=bluetooth \/bluetooth auto defaults,ro,slotselect,nofail,context=u:object_r:bt_firmware_file:s0 0 0#g" ${WORKDIR}/fstab
    #For /run
    sed -i "s#,rootcontext=system_u:object_r:var_run_t:s0##g" ${WORKDIR}/fstab
    # For /var/volatile
    sed -i "s#,rootcontext=system_u:object_r:var_t:s0##g" ${WORKDIR}/fstab
}

do_install[prefuncs] += " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'fix_selinux_labels', '', d)}"
