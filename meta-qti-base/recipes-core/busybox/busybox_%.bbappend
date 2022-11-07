FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_remove = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "file://syslog.cfg", "", d)}"
SRC_URI += "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "file://disable-klog.cfg", "", d)}"

# Only add selinux config if selinux feature is enabled
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'libselinux', '', d)}"
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'file://selinux.cfg', '', d)}"
