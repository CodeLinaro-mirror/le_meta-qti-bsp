FILESBBAPPENDPATH := "${QTI_METAPATH_BASE}/recipes-core/systemd/"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BP}:${FILESBBAPPENDPATH}/${BPN}:"

SRC_URI:append = " file://60-misc.rules"

do_install:append() {
    # Use kernel rules for network iface name
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}${systemd_unitdir}/network/99-default.link

    #Remove privatetmp=true from hostname service
    sed -i  '/^PrivateTmp.*/d' ${D}${systemd_system_unitdir}/systemd-hostnamed.service

    # Remove orignal 60-persistent-v4l.rules which is not applicable for QTI video
    rm ${D}${nonarch_base_libdir}/udev/rules.d/60-persistent-v4l.rules
}
