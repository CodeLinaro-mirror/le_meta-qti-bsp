FILESEXTRAPATHS_append := ":${THISDIR}/systemd-229"

# 0001-sysv-generator-add-default-dependencies.patch depends on 
# 0013-sysv-generator-add-support-for-executing-scripts-und.patch
SRC_URI_append += "file://70-net-setup-link.rules \
                   file://60-persistent-v4l.rules \
                   file://systemd-udev-trigger-full.service \
                   file://0001-sysv-generator-add-default-dependencies.patch \
                   file://0031-udev-trigger-only-enable-must-part-while-leave-other.patch \
                   file://0032-systemd-add-bootkpi-marker-for-login-user-session.patch \
                   file://0030-plymounth-dependency-cleanup.patch \
                   file://0033-systemd-reduce-service-stop-timeout-to-10s.patch \
                   file://0036-systemd-udev-rules-add-Tag-to-diag-device.patch"

python __anonymous () {
    if bb.utils.contains('DISTRO_FEATURES', 'early_init', True, False, d) or bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', True, False, d):
        d.appendVar("SRC_URI", " file://0034-systemd-make-early-init-socket-visible-for-systemd.patch")
        d.appendVar("SRC_URI", " file://0035-systemd-add-handover-support-for-early-service.patch")
}

do_install_append () {
  install -m 0644 ${WORKDIR}/70-net-setup-link.rules ${D}${sysconfdir}/udev/rules.d/
  install -m 0644 ${WORKDIR}/60-persistent-v4l.rules ${D}${sysconfdir}/udev/rules.d/
  install -d ${D}${systemd_unitdir}/system/multi-user.target.wants
  install -m 0644 ${WORKDIR}/systemd-udev-trigger-full.service ${D}${systemd_unitdir}/system/
  ln -sf ../systemd-udev-trigger-full.service ${D}${systemd_unitdir}/system/multi-user.target.wants/systemd-udev-trigger-full.service
}
