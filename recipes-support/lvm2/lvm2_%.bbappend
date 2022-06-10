RDEPENDS_${PN}-scripts_remove += "${@bb.utils.contains_any("DISTRO_FEATURES", "nad-avb nad-fde", "util-linux-findmnt", "", d)}"
