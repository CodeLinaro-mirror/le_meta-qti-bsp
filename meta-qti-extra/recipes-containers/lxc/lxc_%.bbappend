# Fetch code from github
SRCREV = "eaf3c66b93102dd7c093b942443407fbb1a6445f"
SRC_URI_remove= "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz \
                 file://commands-fix-check-for-seccomp-notify-support.patch \
"
SRC_URI_append = "git://github.com/lxc/${BPN}.git;protocol=http;branch=stable-4.0 \
"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append += "\
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb-lxc', 'file://0005-lxc-modify-lxc.service-for-container-avb.patch', '', d)} \
"
S = "${WORKDIR}/git"

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

# Skip wget as license conflicts
RDEPENDS_${PN}_remove = " wget "
# Disable dnsmasq.service in lxc
RDEPENDS_${PN}_remove = "dnsmasq"
