FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://dsi-0-270.cfg \
    file://dsi-1-off.cfg \
"

WESTON_DISPLAYS = "dsi-0-270 dsi-1-off"
