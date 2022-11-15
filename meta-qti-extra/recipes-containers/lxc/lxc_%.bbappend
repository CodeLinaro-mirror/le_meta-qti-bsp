FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        "

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
