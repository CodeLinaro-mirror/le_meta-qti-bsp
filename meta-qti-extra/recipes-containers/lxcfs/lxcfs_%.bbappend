FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Remove 001-systemd-mount-on-var-lib-lxcfs.patch if lxcfs upgrade above 5.0
SRC_URI:append = " \
        file://001-systemd-mount-on-var-lib-lxcfs.patch \
        "
