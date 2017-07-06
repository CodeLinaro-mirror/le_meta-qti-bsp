FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-Fix-adb-shell-env-issue.patch"

EXTRA_OECONF_remove = "--with-logd-logging"

do_install_append() {
   if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
      sed -i -e '/^ExecStart=/i\ExecStartPre=/bin/mkdir -p -m 777 /dev/socket' ${D}/${systemd_unitdir}/system/leprop.service
      sed -i -e 's/\/etc\/init.d/\/etc\/initscripts/' ${D}/${systemd_unitdir}/system/init_post_boot.service
   fi
}

pkg_postinst_${PN}-adbd () {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        if [ -n "$D" ]; then
            OPTS="--root=$D"
        fi
        systemctl $OPTS mask adbd.service
    fi
}

