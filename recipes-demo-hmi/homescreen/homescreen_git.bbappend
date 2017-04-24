FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-Fixed-homescreen-startup.patch"

do_install_append() {
       sed -i -e 's/RestartSec=1/RestartSec=3/' ${D}${systemd_user_unitdir}/WindowManager.service
}
