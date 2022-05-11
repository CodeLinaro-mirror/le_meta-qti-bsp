RDEPENDS_${PN}-scripts_remove += "${@bb.utils.contains("DISTRO_FEATURES", "nad-avb", "util-linux-findmnt", "", d)}"
