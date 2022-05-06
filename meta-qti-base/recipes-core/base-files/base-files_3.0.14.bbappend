FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

DEPENDS += "base-passwd"

SRC_URI:append = " file://${BASEMACHINE}/fstab"

dirs755:append = " \
    /media/cf /media/net /media/ram \
    /media/union /media/realroot /media/hdd /media/mmc1 \
    /firmware /dsp /bluetooth ${localstatedir} /media/card /persist \
    ${userfsdatadir} ${MACHINE_MNT_POINTS} \
"

do_install:append(){
    install -m 755 -o diag -g diag -d ${D}/media
    install -m 755 -o diag -g diag -d ${D}/media/card
    ln -s /media/card ${D}/sdcard
    ln -s ${localstatedir}/run/resolv.conf ${D}${sysconfdir}/resolv.conf
    ln -s ${nonarch_base_libdir} ${D}/lib64
    ln -s ${libdir} ${D}/usr/lib64

    install -m 0644 ${WORKDIR}/fstab ${D}${sysconfdir}/fstab

    # Explicitly remove sepolicy entries from fstab when selinux is not present.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'false', 'true', d)}; then
        #For /run
        sed -i "s#,rootcontext=system_u:object_r:var_run_t:s0##g" ${D}${sysconfdir}/fstab
        # For /var/volatile
        sed -i "s#,rootcontext=system_u:object_r:var_t:s0##g" ${D}${sysconfdir}/fstab
    fi

    # Replace persist/home bind if read-only is not enabled
    if ${@bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', 'false', 'true', d)}; then
        sed -i "/^\PARTLABEL=persist.*var/d" ${D}${sysconfdir}/fstab
        sed -i "/^\#.*Bind/d" ${D}${sysconfdir}/fstab
        sed -i "/^\/data/d" ${D}${sysconfdir}/fstab
        sed -i "/^\${localstatedir}/d" ${D}${sysconfdir}/fstab
    fi
}
