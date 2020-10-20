# Fetch code from github
SRCREV = "98613f618b6f161e11dc9eedf7cb170757624397"
SRC_URI_remove= "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz"
SRC_URI_append = "git://github.com/lxc/${BPN}.git;protocol=http;branch=stable-4.0 \
    "
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append += "\
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        "
S = "${WORKDIR}/git"

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

# Skip wget as license conflicts
RDEPENDS_${PN}_remove = " wget "
