FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0032-systemd-add-bootkpi-marker-for-user-session.patch \
             file://power-switch.rules \
             file://qti_sleep.sh "

do_install_append() {
   install -d ${D}/${base_libdir}/systemd/system-sleep
   install -m 0755 ${WORKDIR}/qti_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh

   # Comment out the validation to selinux user as workaround to login LV host with console at selinux enforcing
   # mode because currently no mapping between linux user and selinux user.
   if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
       sed -i '/pam_selinux/s/^/#/g' ${D}${sysconfdir}/pam.d/systemd-user
   fi
}
