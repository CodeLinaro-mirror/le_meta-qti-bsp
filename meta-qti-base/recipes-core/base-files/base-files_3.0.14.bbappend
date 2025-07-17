FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

DEPENDS += "base-passwd"

SRC_URI:append = " file://fstab"

SRC_URI:append:sa8775 = " file://sh_login"
SRC_URI:append:sa7255 = " file://sh_login"

dirs755:append = " \
    /media/cf /media/net /media/ram \
    /media/union /media/realroot /media/hdd /media/mmc1 \
    /firmware /dsp /bluetooth ${localstatedir} /media/card /persist \
    ${userfsdatadir} ${MACHINE_MNT_POINTS} \
"

dirs755:append:sa8775 = " /data/var_upper /data/var_work "

do_install:append(){
    if(${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)}); then
        ln -s ${nonarch_base_libdir} ${D}/lib64
        ln -s ${libdir} ${D}/usr/lib64
    fi

    if(${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'true', 'false', d)}); then
        install -d ${D}/boot/early-ramdisk
    fi

    install -m 0644 ${WORKDIR}/fstab ${D}${sysconfdir}/fstab

    # Replace persist/home bind if read-only is not enabled
    if ${@bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', 'false', 'true', d)}; then
        sed -i "/^\PARTLABEL=persist.*var/d" ${D}${sysconfdir}/fstab
        sed -i "/^\#.*Bind/d" ${D}${sysconfdir}/fstab
        sed -i "/^\/data/d" ${D}${sysconfdir}/fstab
        sed -i "/^\${localstatedir}/d" ${D}${sysconfdir}/fstab
    fi

}

do_install:append:sa8775() {
    install -m 0755 ${WORKDIR}/sh_login ${D}${base_bindir}/sh_login
}

do_install:append:sa7255() {
    install -m 0755 ${WORKDIR}/sh_login ${D}${base_bindir}/sh_login
}
