
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_remove = "git://github.com/jthornber/thin-provisioning-tools"
SRC_URI_append = "git://github.com/jthornber/thin-provisioning-tools;branch=main;protocol=https"

INSANE_SKIP_${PN} += "already-stripped"
