FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        file://0004-skip-fetch-seccomp-config-on-lxc-without-seccomp.patch \
        file://0006-lxc-modify-lxc-attach-shell-default-context-of-android-container.patch \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb-lxc', 'file://0005-lxc-modify-lxc.service-for-container-avb.patch', '', d)} \
"

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
SYSTEMD_AUTO_ENABLE:${PN}:sa81x5 = "disable"
