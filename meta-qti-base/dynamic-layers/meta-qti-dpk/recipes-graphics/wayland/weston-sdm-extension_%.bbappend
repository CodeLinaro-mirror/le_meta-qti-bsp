FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://override.conf"

IS_VENDOR_PKG = "${@bb.utils.contains('AMZN_VENDOR_PACKAGES_LIST', '${PN}', '1', '', d)}"
SDM_EXT_VENDOR_PREFIX = "${@ d.getVar('AMZN_VENDOR_PREFIX') if d.getVar('IS_VENDOR_PKG') else d.getVar('AMZN_BB_VENDOR_PREFIX') }"
add_weston_drop_in() {
    sed -i s%@sdm_ext_dir%${SDM_EXT_VENDOR_PREFIX}${libdir}/libweston-${WESTON_MAJOR_VERSION}%g ${WORKDIR}/override.conf
    install -D -m 0644 ${WORKDIR}/override.conf ${D}${sysconfdir}/systemd/system/weston@.service.d/override.conf
}

do_install[postfuncs] += "${@ 'add_weston_drop_in' if d.getVar('SDM_EXT_VENDOR_PREFIX') else ''}"

FILES:${PN} += "${@ '${sysconfdir}/systemd/system/weston@.service.d/*' if d.getVar('SDM_EXT_VENDOR_PREFIX') else ''}"
