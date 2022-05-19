FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
        file://0001-kernel-install-shell-changes-to-sh.patch \
        file://0001-Backport-of-networkd-fix.patch \
"

#Add qti specific changes only when qt-disro is enabled.
QTI_SYSTEMD_INC = ""
QTI_SYSTEMD_INC_qti-distro-base = "${THISDIR}/qti-systemd.inc"
include ${QTI_SYSTEMD_INC}


do_install_append() {
    rm -rf ${D}/sbin/reboot

    echo "/sbin/start-stop-daemon -S -b -x /sbin/reboot_post.sh -- \$@" > ${D}/sbin/reboot.sh

    echo "/bin/systemctl stop serial-getty@ttyMSM0" > ${D}/sbin/reboot_post.sh
    echo "/bin/systemctl reboot \$@" >> ${D}/sbin/reboot_post.sh

    chmod u+x ${D}/sbin/reboot.sh
    chmod u+x ${D}/sbin/reboot_post.sh

    ln -s ${D}/sbin/reboot.sh ${D}/sbin/reboot
}

FILES_${PN} += "${base_sbindir}/reboot.sh ${base_sbindir}/reboot_post.sh"

#change the reboot link from /bin/systemctl to /sbin/reboot.sh
ALTERNATIVE_TARGET[reboot] = "${base_sbindir}/reboot.sh"
