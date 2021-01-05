#below package is fetch from CAF
SRC_URI ="git://source.codeaurora.org/quic/le/openssh-portable;nobranch=1;protocol=https;rev=cce8cbe0ed7d1ba3a575310e0b63c193326ae616"

SRC_URI += " \
            file://sshd_config \
            file://ssh_config \
            file://init \
            ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)} \
            file://sshd.socket \
            file://sshd@.service \
            file://sshdgenkeys.service \
            file://volatiles.99_sshd \
            file://run-ptest \
            file://fix-potential-signed-overflow-in-pointer-arithmatic.patch \
            file://sshd_check_keys \
            file://add-test-support-for-busybox.patch \
            "

EXTRA_OECONF_append=" ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '--with-selinux', '', d)}"
BASEPRODUCT = "${@d.getVar('PRODUCT', False)}"

FILESEXTRAPATHS_prepend := "${THISDIR}/openssh:"
SRC_URI += "file://sshd_config"

do_install_append_drone () {
    sed -i -e 's:#PermitRootLogin yes:PermitRootLogin yes:' ${WORKDIR}/sshd_config ${D}${sysconfdir}/ssh/sshd_config
    sed -i -e 's:#PasswordAuthentication yes:PasswordAuthentication yes:' ${WORKDIR}/sshd_config ${D}${sysconfdir}/ssh/sshd_config
}

do_install_append_robot-som () {
    sed -i -e 's:#PasswordAuthentication yes:PasswordAuthentication yes:' ${WORKDIR}/sshd_config ${D}${sysconfdir}/ssh/sshd_config
    sed -i -e 's:#PermitRootLogin yes:PermitRootLogin yes:' ${WORKDIR}/sshd_config ${D}${sysconfdir}/ssh/sshd_config ${D}${sysconfdir}/ssh/sshd_config_readonly
    sed -i -e 's:#PasswordAuthentication yes:PasswordAuthentication yes:' ${WORKDIR}/sshd_config ${D}${sysconfdir}/ssh/sshd_config ${D}${sysconfdir}/ssh/sshd_config_readonly
    sed -i '$a    StrictHostKeyChecking no' ${WORKDIR}/ssh_config ${D}${sysconfdir}/ssh/ssh_config
    sed -i '$a    UserKnownHostsFile /dev/null' ${WORKDIR}/ssh_config ${D}${sysconfdir}/ssh/ssh_config
}

RDEPENDS_${PN} += "${PN}-sftp"
