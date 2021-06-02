RDEPENDS_${PN}_remove = "${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "", "bluez5-obex", d)}"
