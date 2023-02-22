FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

DEPENDS += "base-passwd"

SRC_URI:append = " file://${BASEMACHINE}/fstab"

dirs755:append = " \
    /firmware /dsp ${localstatedir} /persist \
    ${userfsdatadir} ${MACHINE_MNT_POINTS} \
"

do_install:append(){
    ln -s ${nonarch_base_libdir} ${D}/lib64
    ln -s ${libdir} ${D}/usr/lib64

    install -m 0644 ${WORKDIR}/fstab ${D}${sysconfdir}/fstab

    # Replace persist/home bind if read-only is not enabled
    if ${@bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', 'false', 'true', d)}; then
        sed -i "/^\PARTLABEL=persist.*var/d" ${D}${sysconfdir}/fstab
        sed -i "/^\#.*Bind/d" ${D}${sysconfdir}/fstab
        sed -i "/^\/data/d" ${D}${sysconfdir}/fstab
        sed -i "/^\${localstatedir}/d" ${D}${sysconfdir}/fstab
    fi
}
