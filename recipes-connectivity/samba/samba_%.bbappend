FILESEXTRAPATHS_prepend := "${WORKSPACE}/:${THISDIR}"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SYSTEMD_AUTO_ENABLE_${pn}-samba = "disable"


