FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        "

# Disable it on on sa81x5(kernel-5.15) as features are not ready
SRC_URI:remove:sa81x5 = "\
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        "

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
