RDEPENDS:${PN}:remove = "${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "", "bluez5-obex", d)}"
